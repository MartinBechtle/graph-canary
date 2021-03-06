package com.martinbechtle.graphcanary.canary;

import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.warning.Warning;
import com.martinbechtle.graphcanary.warning.WarningService;
import com.martinbechtle.jcanary.api.DependencyStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Bechtle
 */
@Service
public class CanaryService {

    private final GraphService graphService;

    private final WarningService warningService;

    public CanaryService(GraphService graphService, WarningService warningService) {

        this.graphService = graphService;
        this.warningService = warningService;
    }

    public CanaryData getCanaryData() {

        Graph graph = graphService.get();

        Set<String> unhealthyDependencies = new HashSet<>();
        graph.getEdges().forEach(graphEdge -> {
            if (graphEdge.getDependencyStatus() != DependencyStatus.HEALTHY) {
                unhealthyDependencies.add(graphEdge.getFrom());
                unhealthyDependencies.add(graphEdge.getTo());
            }
        });

        DependencyStatus status = DependencyStatus.HEALTHY;
        int totalDependencies = graph.getNodes().size();
        int unhealthyDependencyCount = unhealthyDependencies.size();
        if (unhealthyDependencyCount > 0) {

            boolean moreThanThirtyPercentUnhealthy = unhealthyDependencyCount > (totalDependencies * 0.3);
            status = moreThanThirtyPercentUnhealthy ? DependencyStatus.CRITICAL : DependencyStatus.DEGRADED;
        }

        List<Warning> failedCanaries = warningService.getServiceWarnings();

        return new CanaryData()
                .setGraph(graph)
                .setStatus(status)
                .setFailedCanaries(failedCanaries);
    }
}
