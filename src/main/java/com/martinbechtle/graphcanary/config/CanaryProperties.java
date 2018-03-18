package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.monitor.CanaryEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Bechtle
 */
@ConfigurationProperties(prefix = "canary")
public class CanaryProperties {

    private List<CanaryEndpoint> endpoints = new ArrayList<>();

    private boolean fake = false;

    private int threads = 10;

    public List<CanaryEndpoint> getEndpoints() {

        return endpoints;
    }

    public CanaryProperties setEndpoints(List<CanaryEndpoint> endpoints) {

        this.endpoints = endpoints;
        return this;
    }

    /**
     * If true, it will always return the fake graph instead of pinging actual canaries.
     * Useful for testing and playing around.
     */
    public boolean isFake() {

        return fake;
    }

    public CanaryProperties setFake(boolean fake) {

        this.fake = fake;
        return this;
    }

    public int getThreads() {

        return threads;
    }

    public CanaryProperties setThreads(int threads) {

        this.threads = threads;
        return this;
    }
}
