package com.martinbechtle.graphcanary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by martin on 30/10/2017.
 *
 * @author Martin Bechtle
 */
@ConfigurationProperties(prefix = "canary")
public class CanaryProperties {

    private List<CanaryEndpoint> endpoints;

    public List<CanaryEndpoint> getEndpoints() {

        return endpoints;
    }

    public CanaryProperties setEndpoints(List<CanaryEndpoint> endpoints) {

        this.endpoints = endpoints;
        return this;
    }
}
