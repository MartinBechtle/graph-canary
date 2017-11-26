package com.martinbechtle.graphcanary.config;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by martin on 30/10/2017.
 *
 * @author Martin Bechtle
 */
public class CanaryEndpoint {

    @NotEmpty
    private String name;

    @NotEmpty
    private String url;

    @Min(1)
    @NotNull
    private Integer pollFrequencySec;

    private String secret;

    public String getName() {

        return name;
    }

    public CanaryEndpoint setName(String name) {

        this.name = name;
        return this;
    }

    public String getUrl() {

        return url;
    }

    public CanaryEndpoint setUrl(String url) {

        this.url = url;
        return this;
    }

    public Integer getPollFrequencySec() {

        return pollFrequencySec;
    }

    public CanaryEndpoint setPollFrequencySec(Integer pollFrequencySec) {

        this.pollFrequencySec = pollFrequencySec;
        return this;
    }

    public String getSecret() {

        return secret;
    }

    public CanaryEndpoint setSecret(String secret) {

        this.secret = secret;
        return this;
    }
}
