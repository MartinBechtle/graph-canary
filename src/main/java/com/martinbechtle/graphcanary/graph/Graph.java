package com.martinbechtle.graphcanary.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.martinbechtle.jrequire.Require.notNull;

/**
 * Graph representation of all aggregated data from all canary endpoints
 *
 * @author Martin Bechtle
 */
public class Graph {

    private final List<GraphNode> nodes;

    private final List<GraphEdge> edges;

    @JsonCreator
    public Graph(
            @JsonProperty("nodes") List<GraphNode> nodes,
            @JsonProperty("edges") List<GraphEdge> edges) {

        this.nodes = notNull(nodes);
        this.edges = notNull(edges);
    }

    public List<GraphNode> getNodes() {

        return nodes;
    }

    public List<GraphEdge> getEdges() {

        return edges;
    }
}
