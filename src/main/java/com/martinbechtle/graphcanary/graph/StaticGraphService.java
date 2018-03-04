package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;

import static com.martinbechtle.graphcanary.util.Json.fromJson;
import static com.martinbechtle.graphcanary.util.Resources.resourceAsString;

/**
 * Implementation of {@link GraphService} returning always the same {@link Graph} from a static file.
 * Useful for playing around with the UI and testing.
 *
 * @author Martin Bechtle
 */
public class StaticGraphService implements GraphService {

    private final Graph graph;

    public StaticGraphService() {

        this.graph = fromJson(
                resourceAsString("fake-graph.json", this.getClass()),
                Graph.class
        );
    }

    @Override
    public Graph get() {

        return graph;
    }

    @Override
    public void onCanaryReceived(Canary canary) {

        // do nothing
    }

}
