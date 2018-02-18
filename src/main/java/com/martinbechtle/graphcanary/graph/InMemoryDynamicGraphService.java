package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.Dependency;
import com.martinbechtle.jcanary.api.DependencyStatus;
import com.martinbechtle.jcanary.api.DependencyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Dynamic implementation of {@link GraphService}.
 * Allows for updates of the underlying data and modifies the graph accordingly.
 *
 * @author Martin Bechtle
 */
public class InMemoryDynamicGraphService implements GraphService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryDynamicGraphService.class);

    // maps canaries to service name
    private final ConcurrentHashMap<String, Canary> serviceMap = new ConcurrentHashMap<>();

    private void insertCanaryIntoMap(Canary canary) {

        String serviceName = canary.getServiceName();
        if (isEmpty(serviceName)) {
            logger.warn("Received canary with empty service name. Ignoring.");
        }

        serviceMap.compute(serviceName, (key, existingCanary) -> canary);
    }

    private Graph buildGraphFromMap() {

        if (serviceMap.isEmpty()) {
            return new Graph(emptyList(), emptyList());
        }
        List<GraphNode> serviceNodes = serviceMap.values()
                .stream()
                .map(canary -> new GraphNode(canary.getServiceName(), DependencyType.API))
                .collect(toList());

        Set<GraphNode> allNodes = new HashSet<>();
        serviceMap.values()
                .forEach(canary -> ofNullable(canary.getTweets()).orElse(emptyList())
                        .forEach(healthTweet -> {
                            Dependency dependency = healthTweet.getDependency();
                            GraphNode graphNode = new GraphNode(dependency.getName(), dependency.getType());
                            allNodes.add(graphNode);
                        }));

        allNodes.addAll(serviceNodes);

        // in the current implementation, in case of duplicates, any subsequent edge might be ignored
        // TODO for the future make it so that the least healthy of the two duplicates is picked
        Set<GraphEdge> edges = new HashSet<>();
        serviceMap.values()
                .forEach(canary -> ofNullable(canary.getTweets()).orElse(emptyList())
                        .forEach(healthTweet -> {

                            String from = canary.getServiceName();
                            String to =  healthTweet.getDependency().getName();
                            DependencyStatus status = healthTweet.getResult().getStatus();

                            GraphEdge graphEdge = new GraphEdge(from, to, status);
                            edges.add(graphEdge);
                        }));

        return new Graph(new ArrayList<>(allNodes), emptyList());
    }

    @Override
    public Graph get() {

        return buildGraphFromMap(); // TODO caching mechanism
    }

    @Override
    public void onCanaryReceived(Canary canary) {

        insertCanaryIntoMap(canary);
    }
}
