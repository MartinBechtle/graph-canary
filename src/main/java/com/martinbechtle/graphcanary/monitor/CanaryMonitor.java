package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.graphcanary.config.CanaryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Martin Bechtle
 */
public class CanaryMonitor {

    private static final Logger logger = LoggerFactory.getLogger(CanaryMonitor.class);

    private final ScheduledExecutorService scheduledExecutorService;

    public CanaryMonitor(CanaryProperties properties,
                         ScheduledExecutorService scheduledExecutorService) {

        this.scheduledExecutorService = scheduledExecutorService;

        properties.getEndpoints().forEach(this::scheduleCanaryCheck);
    }

    public Runnable queryCanaryEndpoint(CanaryEndpoint canaryEndpoint) {

        return () -> {
            try {
                doQuery(canaryEndpoint);
            }
            finally {
                scheduleCanaryCheck(canaryEndpoint); // TODO adjust timing
            }
        };
    }

    private void scheduleCanaryCheck(CanaryEndpoint canaryEndpoint) {

        scheduledExecutorService.schedule(
                queryCanaryEndpoint(canaryEndpoint),
                canaryEndpoint.getPollFrequencySec(),
                TimeUnit.SECONDS);
    }

    private void doQuery(CanaryEndpoint canaryEndpoint) {

        // TODO implement check and timeout
        logger.info("Querying " + canaryEndpoint.getName());
    }
}
