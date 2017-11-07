package com.martinbechtle.graphcanary.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.martinbechtle.jcanary.api.DependencyStatus;

import static com.martinbechtle.jrequire.Require.notEmpty;
import static com.martinbechtle.jrequire.Require.notNull;

/**
 * @author Martin Bechtle
 */
public class GraphEdge {

    private final String from;

    private final String to;

    private final DependencyStatus dependencyStatus;

    @JsonCreator
    public GraphEdge(
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("status") DependencyStatus dependencyStatus) {

        this.from = notEmpty(from);
        this.to = notEmpty(to);
        this.dependencyStatus = notNull(dependencyStatus);
    }

    public String getFrom() {

        return from;
    }

    public String getTo() {

        return to;
    }

    public DependencyStatus getDependencyStatus() {

        return dependencyStatus;
    }

    @Override
    public String toString() {

        return "GraphEdge{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", dependencyStatus=" + dependencyStatus +
                '}';
    }
}
