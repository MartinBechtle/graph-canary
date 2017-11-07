package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;

import static java.util.Collections.emptyList;

/**
 * Dynamic implementation of {@link GraphService}.
 * Allows for updates of the underlying data and modifies the graph accordingly.
 *
 * @author Martin Bechtle
 */
public class InMemoryDynamicGraphService implements GraphService {

    @Override
    public Graph get() {

        return new Graph(emptyList(), emptyList());
    }

    @Override
    public void onCanaryReceived(Canary canary) {

    }
}
