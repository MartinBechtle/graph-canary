package com.martinbechtle.graphcanary.graph;

/**
 * Responsible for providing the {@link Graph} with all the services/dependencies
 *
 * @author Martin Bechtle
 */
public interface GraphProvider {

    Graph get();
}
