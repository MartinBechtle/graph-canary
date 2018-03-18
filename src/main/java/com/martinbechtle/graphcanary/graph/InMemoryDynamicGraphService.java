package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.Dependency;
import com.martinbechtle.jcanary.api.DependencyType;
import com.martinbechtle.jcanary.api.HealthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
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
        Map<String, GraphNode> graphNodesMappedByDependencyName = new HashMap<>();

        // in the current implementation, in case of duplicates, any subsequent edge might be ignored
        // TODO for the future make it so that the least healthy of the two duplicates is picked
        Set<GraphEdge> edges = new HashSet<>();
        // set relies on a particular implementation of hash and equals on GraphNode to avoid duplicates even in case of inverted from/to

        // iterating over this safe, but might not "see" any changes done concurrently on the map, which is fine
        serviceMap.values()
                .forEach(canary -> {

                    // add the service itself to map
                    String serviceName = canary.getServiceName();
                    graphNodesMappedByDependencyName.put(serviceName, new GraphNode(serviceName, DependencyType.API));

                    // add all the services dependencies to map
                    ofNullable(canary.getTweets()).orElse(emptyList())
                            .forEach(healthTweet -> {
                                Dependency dependency = healthTweet.getDependency();
                                GraphNode graphNode = new GraphNode(dependency.getName(), dependency.getType());
                                graphNodesMappedByDependencyName.put(dependency.getName(), graphNode);

                                String from = canary.getServiceName();
                                String to = healthTweet.getDependency().getName();
                                HealthResult healthResult = healthTweet.getResult();

                                GraphEdge graphEdge = new GraphEdge(from, to,
                                        healthResult.getStatus(),
                                        healthResult.getStatusText());

                                edges.add(graphEdge);
                            });
                });

        List<GraphNode> allNodes = new ArrayList<>(graphNodesMappedByDependencyName.values());

        return new Graph(new ArrayList<>(allNodes), new ArrayList<>(edges));
    }

    @Override
    public Graph get() {

        return buildGraphFromMap(); // TODO caching mechanism?
    }

    @Override
    public void onCanaryReceived(Canary canary) {

        insertCanaryIntoMap(canary);
    }
}
