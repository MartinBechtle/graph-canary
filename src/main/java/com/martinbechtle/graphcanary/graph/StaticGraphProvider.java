package com.martinbechtle.graphcanary.graph;

/**
 * Implementation of {@link GraphProvider} returning always the same {@link Graph} from a static file.
 * Useful for playing around with the UI and testing.
 *
 * @author Martin Bechtle
 */
public class StaticGraphProvider implements GraphProvider {

    @Override
    public Graph get() {

        return new Graph();
    }
}
