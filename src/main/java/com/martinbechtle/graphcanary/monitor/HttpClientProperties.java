package com.martinbechtle.graphcanary.monitor;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the http client to be used in the {@link CanaryMonitor}
 */
@ConfigurationProperties(prefix = "canaryMonitor.httpClient")
public class HttpClientProperties {

    private int maxConnections = 10;
    private long keepAliveDurationInMillis = 10000;
    private long connectTimeoutInMillis = 10000;
    private long readTimeoutInMillis = 10000;
    private long writeTimeoutInMillis = 10000;

    public int getMaxConnections() {

        return maxConnections;
    }

    public HttpClientProperties setMaxConnections(int maxConnections) {

        this.maxConnections = maxConnections;
        return this;
    }

    public long getKeepAliveDurationInMillis() {

        return keepAliveDurationInMillis;
    }

    public HttpClientProperties setKeepAliveDurationInMillis(long keepAliveDurationInMillis) {

        this.keepAliveDurationInMillis = keepAliveDurationInMillis;
        return this;
    }

    public long getConnectTimeoutInMillis() {

        return connectTimeoutInMillis;
    }

    public HttpClientProperties setConnectTimeoutInMillis(long connectTimeoutInMillis) {

        this.connectTimeoutInMillis = connectTimeoutInMillis;
        return this;
    }

    public long getReadTimeoutInMillis() {

        return readTimeoutInMillis;
    }

    public HttpClientProperties setReadTimeoutInMillis(long readTimeoutInMillis) {

        this.readTimeoutInMillis = readTimeoutInMillis;
        return this;
    }

    public long getWriteTimeoutInMillis() {

        return writeTimeoutInMillis;
    }

    public HttpClientProperties setWriteTimeoutInMillis(long writeTimeoutInMillis) {

        this.writeTimeoutInMillis = writeTimeoutInMillis;
        return this;
    }
}
