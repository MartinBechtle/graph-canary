package com.martinbechtle.graphcanary.rest;

/**
 * Provides information about the URLs of the registered endpoints.
 *
 * @author Rafael Tedin Alvarez
 */
public interface PathProvider {

    String getUrl(String path, int serverPort);
}