package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jrequire.Require;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Martin Bechtle
 */
@Component
public class CanaryHttpClient {

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public CanaryHttpClient(OkHttpClient httpClient, ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    // TODO javadoc
    public Canary getCanary(CanaryEndpoint canaryEndpoint) throws IOException {

        Require.notNull(canaryEndpoint);

        Request request = new Request.Builder()
                .url(canaryEndpoint.getUrl())
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            Canary canary = objectMapper.readValue(response.body().bytes(), Canary.class);
            return Canary.error("error"); // TODO
        }
        catch (IOException e) {
            throw new RuntimeException(e); // TODO more specific?
        }
    }
}
