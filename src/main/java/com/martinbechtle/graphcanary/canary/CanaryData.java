package com.martinbechtle.graphcanary.canary;

import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.jcanary.api.DependencyStatus;

/**
 * @author Martin Bechtle
 */
public class CanaryData {

    private Graph graph;

    /**
     * Status of the system.
     * <ul>
     *     <li><em>healthy</em> if all services healthy</li>
     *     <li><em>degraded</em> if at least one service not healthy</li>
     *     <li><em>critical</em> if all more than 30% of services not healthy</li>
     * </ul>
     */
    private DependencyStatus status;

    public Graph getGraph() {

        return graph;
    }

    public CanaryData setGraph(Graph graph) {

        this.graph = graph;
        return this;
    }

    public DependencyStatus getStatus() {

        return status;
    }

    public CanaryData setStatus(DependencyStatus status) {

        this.status = status;
        return this;
    }
}
