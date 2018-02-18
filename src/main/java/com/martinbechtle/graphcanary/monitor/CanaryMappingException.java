package com.martinbechtle.graphcanary.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Thrown in case a Canary is being deserialized but has wrong format and thus cannot be mapped to a Java object
 *
 * @author Martin Bechtle
 */
public class CanaryMappingException extends RuntimeException {

    public CanaryMappingException(JsonProcessingException e) {

        super(e);
    }
}
