package com.martinbechtle.graphcanary.graph;

/**
 * Dynamic implementation of {@link GraphProvider}.
 * Allows for updates of the underlying data and modifies the graph accordingly.
 *
 * @author Martin Bechtle
 */
public class InMemoryDynamicGraphProvider implements GraphProvider {

    @Override
    public Graph get() {

        return new Graph();
    }
}
