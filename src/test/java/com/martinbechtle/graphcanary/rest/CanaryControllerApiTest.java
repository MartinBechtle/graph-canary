package com.martinbechtle.graphcanary.rest;

import com.martinbechtle.graphcanary.canary.CanaryData;
import com.martinbechtle.graphcanary.canary.CanaryService;
import com.martinbechtle.graphcanary.config.ApiTest;
import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.graphcanary.graph.GraphNode;
import com.martinbechtle.graphcanary.warning.Warning;
import com.martinbechtle.jcanary.api.DependencyStatus;
import com.martinbechtle.jcanary.api.DependencyType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.when;
import static com.martinbechtle.graphcanary.JsonMatcher.hasSameContentAs;
import static com.martinbechtle.jcanary.api.CanaryResult.ERROR;
import static com.martinbechtle.jcanary.api.DependencyStatus.HEALTHY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

/**
 * Api test for {@link CanaryController}
 *
 * @author Martin Bechtle
 */
@RunWith(SpringRunner.class)
@ApiTest
public class CanaryControllerApiTest extends BaseApiTest {

    @Autowired
    private CanaryService canaryService;

    @Test
    public void getCanaryData() throws IOException {

        when(canaryService.getCanaryData())
                .thenReturn(new CanaryData()
                        .setGraph(fakeGraph())
                        .setStatus(HEALTHY)
                        .setFailedCanaries(singletonList(new Warning("node4", ERROR)))
                );

        when().get(getUrl("/graph"))
                .then()
                .log().all()
                .statusCode(200)
                .body(hasSameContentAs(getClass(), "Graph_OkResponse.json"));
    }

    private Graph fakeGraph() {

        return new Graph(
                asList(
                        new GraphNode("node1", DependencyType.API),
                        new GraphNode("node2", DependencyType.WORKER),
                        new GraphNode("node3", DependencyType.DATABASE)
                ),
                asList(
                        new GraphEdge("from1", "to1", DependencyStatus.DEGRADED, "degraded"),
                        new GraphEdge("from1", "to3", HEALTHY, "")
                ));
    }
}