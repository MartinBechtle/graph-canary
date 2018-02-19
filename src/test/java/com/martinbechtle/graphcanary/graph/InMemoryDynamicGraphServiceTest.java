package com.martinbechtle.graphcanary.graph;

import com.martinbechtle.jcanary.api.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

        graphService.onCanaryReceived(new Canary("service-1", CanaryResult.OK, Arrays.asList(
                new HealthTweet(
                        new Dependency(DependencyImportance.PRIMARY, DependencyType.API, "dependency-1-1"),
                        new HealthResult(DependencyStatus.HEALTHY, ""),
                        100),
                new HealthTweet(
                        new Dependency(DependencyImportance.SECONDARY, DependencyType.FTP, "dependency-1-2"),
                        new HealthResult(DependencyStatus.DEGRADED, ""),
                        200)
        )));
        graphService.onCanaryReceived(new Canary("service-2", CanaryResult.OK, Arrays.asList(
                new HealthTweet(
                        new Dependency(DependencyImportance.PRIMARY, DependencyType.API, "dependency-1-1"),
                        new HealthResult(DependencyStatus.HEALTHY, ""),
                        100),
                new HealthTweet(
                        new Dependency(DependencyImportance.PRIMARY, DependencyType.CACHE, "dependency-2-1"),
                        new HealthResult(DependencyStatus.HEALTHY, ""),
                        250)
        )));

        Graph graph = graphService.get();

        assertThat(graph.getNodes()).hasSize(5);

        GraphNode service1 = assertContains(graph.getNodes(), "service-1");
        assertThat(service1.getType()).isEqualTo(DependencyType.API);
        GraphNode service2 = assertContains(graph.getNodes(), "service-2");
        assertThat(service2.getType()).isEqualTo(DependencyType.API);
        GraphNode dependency11 = assertContains(graph.getNodes(), "dependency-1-1");
        assertThat(dependency11.getType()).isEqualTo(DependencyType.API);
        GraphNode dependency12 = assertContains(graph.getNodes(), "dependency-1-2");
        assertThat(dependency12.getType()).isEqualTo(DependencyType.FTP);
        GraphNode dependency21 = assertContains(graph.getNodes(), "dependency-2-1");
        assertThat(dependency21.getType()).isEqualTo(DependencyType.CACHE);


//        assertThat(graph.getEdges()).hasSize(4);
    }

    private GraphNode assertContains(List<GraphNode> nodeList, String name) {

        return nodeList.stream()
                .filter(graphNode -> name.equals(graphNode.getName()))
                .findAny()
                .orElseThrow(AssertionError::new);
    }
}