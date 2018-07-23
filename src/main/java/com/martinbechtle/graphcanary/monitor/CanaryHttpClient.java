package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jrequire.Require;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Martin Bechtle
 */
@Component
public class CanaryHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(CanaryHttpClient.class);

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public CanaryHttpClient(OkHttpClient httpClient, ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * @throws CanaryHttpException in case of network error or non-200 response
     * @throws CanaryMappingException if the response body cannot be mapped to a {@link Canary}
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

            int responseCode = response.code();

            if (response.isSuccessful() || isUnauthorized(responseCode)) {

                String responseBody = new String(response.body().bytes());
                logger.trace("Received body from {}:\n{}", canaryEndpoint.getName(), responseBody);
                return objectMapper.readValue(responseBody, Canary.class);
            }
            logger.warn("Non-successful {} http response while retrieving canary at {}",
                    responseCode, canaryEndpoint.getUrl());
            throw new CanaryHttpException("Status: " + responseCode);
        }
        catch (JsonProcessingException e) {
            logger.warn("Invalid response body found while retrieving canary at {}", canaryEndpoint.getUrl());
            throw new CanaryMappingException(e);
        }
        catch (IOException e) {
            logger.warn("Network exception while retrieving canary at {}", canaryEndpoint.getUrl());
            throw new CanaryHttpException(e);
        }
    }

    private static boolean isUnauthorized(int code) {

        return code == 401 || code == 403;
    }
}
