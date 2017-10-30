package com.martinbechtle.graphcanary.graph;

/**
 * Graph representation of all aggregated data from all canary endpoints
 *
 * @author Martin Bechtle
 */
public class Graph {

    private String name = "test";

    public String getName() {

        return name;
    }

    public Graph setName(String name) {

        this.name = name;
        return this;
    }
}
