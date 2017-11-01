package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;

/**
 * Implementation of {@link GraphService} returning always the same {@link Graph} from a static file.
 * Useful for playing around with the UI and testing.
 *
 * @author Martin Bechtle
 */
public class StaticGraphService implements GraphService {

    @Override
    public Graph get() {

        return new Graph();
    }

    @Override
    public void onCanaryReceived(Canary canary) {

    }

}
