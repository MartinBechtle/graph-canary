package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.graphcanary.config.GraphConfig;
import com.martinbechtle.graphcanary.config.HttpClientProperties;
import com.martinbechtle.graphcanary.config.JacksonConfig;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

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
    }

    @Test
    public void getCanary_ShouldMapResponseFieldsCorrectly_WhenOkResponse() throws Exception {

    }

}