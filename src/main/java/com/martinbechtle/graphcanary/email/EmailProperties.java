package com.martinbechtle.graphcanary.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author martin
 */
@ConfigurationProperties(prefix = "canary.email")
public class EmailProperties {

    private String to;

    private String from;

    public String getTo() {

        return to;
    }

    public EmailProperties setTo(String to) {

        this.to = to;
        return this;
    }

    public String getFrom() {

        return from;
    }

    public EmailProperties setFrom(String from) {

        this.from = from;
        return this;
    }
}
