package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.graph.InMemoryDynamicGraphService;
import com.martinbechtle.graphcanary.graph.StaticGraphService;
import com.martinbechtle.graphcanary.monitor.CanaryMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * @author Martin Bechtle
 */
@Configuration
@EnableScheduling
public class GraphConfig {

    private static final Logger logger = LoggerFactory.getLogger(GraphConfig.class);

    @Bean
    public GraphService graphProvider(CanaryProperties canaryProperties) {

        if (canaryProperties.isFake()) {

            logger.info("Using static graph service with a fake graph. " +
                    "If this is unwanted please set property canary.fake to false");

            return new StaticGraphService();
        }
        return new InMemoryDynamicGraphService();
    }

    @Bean
    public CanaryMonitor canaryMonitor(CanaryProperties canaryProperties) {

        if (canaryProperties.isFake()) {
            return null;
        }
        int threadPoolSize = canaryProperties.getThreads();

        logger.info("Initializing CanaryMonitor with {} threads", threadPoolSize);

        return new CanaryMonitor(
                canaryProperties,
                newScheduledThreadPool(threadPoolSize)
        );
    }
}
