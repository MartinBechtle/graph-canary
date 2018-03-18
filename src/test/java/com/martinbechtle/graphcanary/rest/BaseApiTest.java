package com.martinbechtle.graphcanary.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;

import java.util.Optional;

/**
 * @author Martin Bechtle
 */
public class BaseApiTest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    protected PathProvider pathProvider;

    protected String getUrl(String relativeUrl) {

        String sanitizedRelativeUrl = Optional.ofNullable(relativeUrl)
                .map(s -> s.startsWith("/") ? s.substring(1) : s)
                .orElse("");

        return pathProvider.getUrl(sanitizedRelativeUrl, serverPort);
    }
}
