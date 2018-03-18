package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.DependencyStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link GraphEdge}
 *
 * @author Martin Bechtle
 */
public class GraphEdgeTest {

    @Test
    public void equals_ShouldReturnTrue_WhenFromAndToAreSame() {

        GraphEdge graphEdge1 = new GraphEdge("from", "to", DependencyStatus.HEALTHY, "something");
        GraphEdge graphEdge2 = new GraphEdge("from", "to", DependencyStatus.DEGRADED, "something else");
        assertThat(graphEdge1).isEqualTo(graphEdge2);
    }

    @Test
    public void equals_ShouldReturnTrue_WhenFromAndToAreSameInverted() {

        GraphEdge graphEdge1 = new GraphEdge("from", "to", DependencyStatus.HEALTHY, "");
        GraphEdge graphEdge2 = new GraphEdge("to", "from", DependencyStatus.DEGRADED, "");
        assertThat(graphEdge1).isEqualTo(graphEdge2);
    }

    @Test
    public void hashCode_ShouldBeSame_ForEqualGraphNodes() {

        GraphEdge graphEdge1 = new GraphEdge("from", "to", DependencyStatus.HEALTHY, "");
        GraphEdge graphEdge2 = new GraphEdge("to", "from", DependencyStatus.DEGRADED, "");
        assertThat(graphEdge1.hashCode()).isEqualTo(graphEdge2.hashCode());
    }
}