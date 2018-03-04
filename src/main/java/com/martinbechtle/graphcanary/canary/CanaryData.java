package com.martinbechtle.graphcanary.canary;

import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.graphcanary.warning.Warning;
import com.martinbechtle.jcanary.api.CanaryResult;
import com.martinbechtle.jcanary.api.DependencyStatus;

import java.util.List;

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

    /**
     * List of known services for which retrieving information from the canary endpoint has failed.
     * <p>
     * Failure can happen for a number of reasons, see {@link CanaryResult}.
     */
    private List<Warning> failedCanaries;

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

    public List<Warning> getFailedCanaries() {

        return failedCanaries;
    }

    public CanaryData setFailedCanaries(List<Warning> failedCanaries) {

        this.failedCanaries = failedCanaries;
        return this;
    }
}
