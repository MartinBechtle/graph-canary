package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.monitor.CanaryMonitor;
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

    public void setMaxConnections(int maxConnections) {

        this.maxConnections = maxConnections;
    }

    public long getKeepAliveDurationInMillis() {

        return keepAliveDurationInMillis;
    }

    public void setKeepAliveDurationInMillis(long keepAliveDurationInMillis) {

        this.keepAliveDurationInMillis = keepAliveDurationInMillis;
    }

    public long getConnectTimeoutInMillis() {

        return connectTimeoutInMillis;
    }

    public void setConnectTimeoutInMillis(long connectTimeoutInMillis) {

        this.connectTimeoutInMillis = connectTimeoutInMillis;
    }

    public long getReadTimeoutInMillis() {

        return readTimeoutInMillis;
    }

    public void setReadTimeoutInMillis(long readTimeoutInMillis) {

        this.readTimeoutInMillis = readTimeoutInMillis;
    }

    public long getWriteTimeoutInMillis() {

        return writeTimeoutInMillis;
    }

    public void setWriteTimeoutInMillis(long writeTimeoutInMillis) {

        this.writeTimeoutInMillis = writeTimeoutInMillis;
    }
}
