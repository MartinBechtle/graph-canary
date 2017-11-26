package com.martinbechtle.graphcanary.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Martin Bechtle
 */
public class Resources {

    public static String resourceAsString(String resourcePath, String errorMsgOnFailure) {
        try (InputStream inputStream = new ClassPathResource(resourcePath).getInputStream()) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(errorMsgOnFailure, e);
        }
    }

    public static String resourceAsString(String resourcePath) {
        try (InputStream inputStream = new ClassPathResource(resourcePath).getInputStream()) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + resourcePath, e);
        }
    }

    public static String resourceAsString(String resourceName, Class<?> classInSamePackage, String errorMsgOnFailure) {
        try (InputStream inputStream = new ClassPathResource(resourceName, classInSamePackage).getInputStream()) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(errorMsgOnFailure, e);
        }
    }

    public static String resourceAsString(String resourceName, Class<?> classInSamePackage) {
        return resourceAsString(resourceName, classInSamePackage, "Error loading " + resourceName);
    }
}
