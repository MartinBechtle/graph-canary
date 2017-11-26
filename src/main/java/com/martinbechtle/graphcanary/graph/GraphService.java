package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;

/**
 * Responsible for providing and maintaining the {@link Graph} with all the services/dependencies
 *
 * @author Martin Bechtle
 */
public interface GraphService {

    /**
     * @return the current {@link Graph} based on received {@link Canary}s
     */
    Graph get();

    /**
     * To be invoked when a new {@link Canary} is received from a service. The underlying graph will be updated.
     */
    void onCanaryReceived(Canary canary);
}
