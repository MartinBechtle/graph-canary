package com.martinbechtle.graphcanary.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.martinbechtle.jcanary.api.DependencyStatus;

import java.util.Objects;
import java.util.Optional;

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

    /**
     * custom built to ensure equality in case of inverted from-to and ignore dependencyStatus
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphEdge)) {
            return false;
        }
        GraphEdge graphEdge = (GraphEdge) o;
        return Objects.equals(fromToCombined(), graphEdge.fromToCombined());
    }

    /**
     * custom built to ensure equality in case of inverted from-to and ignore dependencyStatus
     */
    @Override
    public int hashCode() {

        if (from == null || to == null) {
            return Objects.hash(from, to);
        }
        return Objects.hashCode(fromToCombined());
    }

    private String fromToCombined() {

        String safeFrom = Optional.ofNullable(from).orElse("");
        String safeTo = Optional.ofNullable(to).orElse("");
        return safeFrom.compareTo(safeTo) < 1 ? safeFrom + safeTo : safeTo + safeFrom;
    }
}
