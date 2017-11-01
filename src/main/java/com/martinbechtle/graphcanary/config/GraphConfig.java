package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.graph.StaticGraphService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Martin Bechtle
 */
@Configuration
public class GraphConfig {

    @Bean
    public GraphService graphProvider(CanaryProperties canaryProperties) {

        return new StaticGraphService();
    }
}
