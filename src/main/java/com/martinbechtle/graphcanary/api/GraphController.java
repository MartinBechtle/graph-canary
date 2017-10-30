package com.martinbechtle.graphcanary.api;

import com.martinbechtle.graphcanary.graph.Graph;
import com.martinbechtle.graphcanary.graph.GraphProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GraphController {

    private final GraphProvider graphProvider;

    @Autowired
    public GraphController(GraphProvider graphProvider) {

        this.graphProvider = graphProvider;
    }

    @GetMapping
    public Graph getGraph() {

        return graphProvider.get();
    }
}
