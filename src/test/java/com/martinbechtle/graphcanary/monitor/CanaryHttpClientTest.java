package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.graphcanary.config.GraphConfig;
import com.martinbechtle.graphcanary.config.HttpClientProperties;
import com.martinbechtle.graphcanary.config.JacksonConfig;
import com.martinbechtle.jcanary.api.*;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.martinbechtle.graphcanary.util.Resources.resourceAsString;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link CanaryHttpClient} using the actual {@link ObjectMapper} of this application,
 * against a mock web server. Does not require spinning up the entire spring application.
 *
 * @author Martin Bechtle
 */
public class CanaryHttpClientTest {

    private MockWebServer server;

    private CanaryHttpClient canaryHttpClient;

    @Before
    public void setUp() throws IOException {

        OkHttpClient okHttpClient = new GraphConfig().okHttpClient(
                new HttpClientProperties()
                        .setConnectTimeoutInMillis(500));

        ObjectMapper objectMapper = new JacksonConfig().objectMapper();
        canaryHttpClient = new CanaryHttpClient(okHttpClient, objectMapper);

        server = new MockWebServer();
        server.start();
    }

    @Test
    public void getCanary_ShouldGetSpecifiedUrl_WithSecretAsQueryParam() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        CanaryEndpoint canaryEndpoint = new CanaryEndpoint()
                .setUrl(server.url("/canary").toString())
                .setSecret("october");

        canaryHttpClient.getCanary(canaryEndpoint);

        RecordedRequest request = server.takeRequest();

        assertThat(request.getMethod()).isEqualToIgnoringCase("get");
        assertThat(request.getPath()).isEqualTo("/canary");
        assertThat(request.getHeader("Authorization")).isEqualTo("october");
    }

    @Test
    public void getCanary_ShouldMapResponseFieldsCorrectly_WhenOkResponse() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resourceAsString("Canary_OkResponse.json", getClass()))
        );
        CanaryEndpoint canaryEndpoint = new CanaryEndpoint()
                .setUrl(server.url("/canary").toString());

        Canary canary = canaryHttpClient.getCanary(canaryEndpoint);

        List<HealthTweet> expectedTweets = Arrays.asList(
                new HealthTweet(
                        new Dependency(DependencyImportance.PRIMARY, DependencyType.RESOURCE, "dummyMonitor1"),
                        HealthResult.ok(),
                        20500),
                new HealthTweet(
                        new Dependency(DependencyImportance.SECONDARY, DependencyType.WORKER, "dummyMonitor2"),
                        HealthResult.of(DependencyStatus.DEGRADED, "I'm broken"),
                        1)
        );

        assertThat(canary).isNotNull();
        assertThat(canary.getServiceName()).isEqualTo("test-service");
        assertThat(canary.getResult()).isEqualTo(CanaryResult.OK);
        assertThat(canary.getTweets()).isEqualTo(expectedTweets);
    }

    // TODO
    public void getCanary_ReturnErrorCanary_WhenOkResponseWithMissingData() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resourceAsString("Canary_OkResponse.json", getClass()))
        );
        CanaryEndpoint canaryEndpoint = new CanaryEndpoint()
                .setUrl(server.url("/canary").toString());

        Canary canary = canaryHttpClient.getCanary(canaryEndpoint);

        assertThat(canary).isNotNull();
        assertThat(canary.getServiceName()).isEqualTo("test-service");
        assertThat(canary.getResult()).isEqualTo(CanaryResult.ERROR);
    }

    // TODO
    public void getCanary_ReturnErrorCanary_WhenOkResponseWithInvalidData() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resourceAsString("Canary_OkResponse.json", getClass()))
        );
        CanaryEndpoint canaryEndpoint = new CanaryEndpoint()
                .setUrl(server.url("/canary").toString());

        Canary canary = canaryHttpClient.getCanary(canaryEndpoint);

        assertThat(canary).isNotNull();
        assertThat(canary.getServiceName()).isEqualTo("test-service");
        assertThat(canary.getResult()).isEqualTo(CanaryResult.ERROR);
    }

}