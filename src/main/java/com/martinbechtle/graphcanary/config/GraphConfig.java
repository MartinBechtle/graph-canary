package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.graph.GraphProvider;
import com.martinbechtle.graphcanary.graph.StaticGraphProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Martin Bechtle
 */
@Configuration
public class GraphConfig {

    @Bean
    public GraphProvider graphProvider(CanaryProperties canaryProperties) {

        return new StaticGraphProvider();
    }
}
