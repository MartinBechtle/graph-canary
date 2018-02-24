package com.martinbechtle.graphcanary.api;

import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.graphcanary.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the {@link Graph} as json
 *
 * @author Martin Bechtle
 */
@RestController
@RequestMapping("/graph")
@CrossOrigin
public class GraphController {

    private final GraphService graphService;

    @Autowired
    public GraphController(GraphService graphService) {

        this.graphService = graphService;
    }

    @GetMapping
    public Graph getGraph() {

        return graphService.get();
    }
}
