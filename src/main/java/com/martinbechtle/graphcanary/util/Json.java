package com.martinbechtle.graphcanary.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Static Json utilities
 *
 * @author Martin Bechtle
 */
public class Json {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static String json(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}
