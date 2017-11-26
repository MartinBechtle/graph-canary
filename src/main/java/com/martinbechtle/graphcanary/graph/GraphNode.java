package com.martinbechtle.graphcanary.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.martinbechtle.jcanary.api.DependencyType;

import static com.martinbechtle.jrequire.Require.notEmpty;
import static com.martinbechtle.jrequire.Require.notNull;

/**
 * @author Martin Bechtle
 */
public class GraphNode {

    private final String name;

    private final DependencyType type;

    @JsonCreator
    public GraphNode(
            @JsonProperty("name") String name,
            @JsonProperty("type") DependencyType type) {

        this.name = notEmpty(name);
        this.type = notNull(type);
    }

    public String getName() {

        return name;
    }

    public DependencyType getType() {

        return type;
    }
}
