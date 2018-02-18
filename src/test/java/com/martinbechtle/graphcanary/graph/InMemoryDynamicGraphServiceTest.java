package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.CanaryResult;
import com.martinbechtle.jcanary.api.DependencyType;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link InMemoryDynamicGraphService}
 *
 * @author Martin Bechtle
 */
public class InMemoryDynamicGraphServiceTest {

    private InMemoryDynamicGraphService graphService = new InMemoryDynamicGraphService();

    @Test
    public void get_ShouldReturnEmptyGraph_WhenNoCanariesReceived() {

        Graph graph = graphService.get();
        assertThat(graph).isNotNull();
        assertThat(graph.getEdges()).isEmpty();
        assertThat(graph.getNodes()).isEmpty();
    }

    @Test
    public void get_ShouldReturnGraphWithOneApiNodeAndNoEdges_WhenOneCanaryReceivedWithError() {

        final String SERVICE_NAME = "service-1";

        graphService.onCanaryReceived(new Canary(SERVICE_NAME, CanaryResult.ERROR, emptyList()));
        Graph graph = graphService.get();

        assertThat(graph.getEdges()).isEmpty();
        assertThat(graph.getNodes()).hasSize(1);
        assertThat(graph.getNodes().get(0).getName()).isEqualTo(SERVICE_NAME);
        assertThat(graph.getNodes().get(0).getType()).isEqualTo(DependencyType.API);
    }

    @Test
    public void get_ShouldReturnGraphWithNodesAndEdges_WhenMultipleCanariesReceived() {

        // TODO
    }
}