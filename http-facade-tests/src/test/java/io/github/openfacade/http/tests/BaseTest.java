package io.github.openfacade.http.tests;

import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.HttpClientEngine;
import io.github.openfacade.http.HttpServerConfig;
import io.github.openfacade.http.HttpServerEngine;
import io.github.openfacade.http.ReactorHttpClientConfig;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected List<HttpClientConfig> clientConfigList() {
        return List.of(
                new HttpClientConfig.Builder().engine(HttpClientEngine.AsyncHttpClient).build(),
                new HttpClientConfig.Builder().engine(HttpClientEngine.JAVA).build(),
                new HttpClientConfig.Builder().engine(HttpClientEngine.JAVA8).build(),
                new HttpClientConfig.Builder().engine(HttpClientEngine.OkHttp).build(),
                new HttpClientConfig.Builder().engine(HttpClientEngine.Vertx).build()
        );
    }

    protected List<HttpServerConfig> serverConfigList() {
        return List.of(
                new HttpServerConfig.Builder().host("127.0.0.1").engine(HttpServerEngine.Jetty).build(),
                new HttpServerConfig.Builder().host("127.0.0.1").engine(HttpServerEngine.Tomcat).build(),
                new HttpServerConfig.Builder().host("127.0.0.1").engine(HttpServerEngine.Vertx).build()
        );
    }

    protected ReactorHttpClientConfig reactorHttpClientConfig() {
        ReactorHttpClientConfig reactorClientConfig = new ReactorHttpClientConfig.Builder().build();
        return reactorClientConfig;
    }

    protected Stream<Arguments> clientServerConfigProvider() {
        List<HttpClientConfig> httpClientConfigs = clientConfigList();
        List<HttpServerConfig> httpServerConfigs = serverConfigList();

        return httpClientConfigs.stream()
                .flatMap(clientConfig -> httpServerConfigs.stream()
                        .map(serverConfig -> Arguments.arguments(clientConfig, serverConfig))
                );
    }

    protected Stream<Arguments> reactorClientServerConfigProvider() {
        List<HttpServerConfig> httpServerConfigs = serverConfigList();
        ReactorHttpClientConfig reactorClientConfig = reactorHttpClientConfig();
        return httpServerConfigs.stream().map(serverConfig -> Arguments.arguments(reactorClientConfig, serverConfig));
    }
}
