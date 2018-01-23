package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.CanaryResult;
import com.martinbechtle.jrequire.Require;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

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

    /**
     * @return a {@link Canary} of result {@link CanaryResult#OK} when the HTTP call to the canary endpoint was successful
     * and the response body could be understood.
     */
    public Canary getCanary(CanaryEndpoint canaryEndpoint) {

        Require.notNull(canaryEndpoint);

        Request.Builder builder = new Request.Builder()
                .url(canaryEndpoint.getUrl())
                .get();

        Optional.ofNullable(canaryEndpoint.getSecret()).ifPresent(secret ->
                builder.header("Authorization", secret));

        Request request = builder.build();

        try (Response response = httpClient.newCall(request).execute()) {

            return objectMapper.readValue(response.body().bytes(), Canary.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e); // TODO more specific?
        }
    }
}
