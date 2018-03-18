package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.graphcanary.config.CanaryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * @author Martin Bechtle
 */
public class CanaryMonitor {

    private static final Logger logger = LoggerFactory.getLogger(CanaryMonitor.class);

    private final ScheduledExecutorService scheduledExecutorService;

    private final CanaryRetriever canaryRetriever;

    public CanaryMonitor(CanaryProperties properties,
                         ScheduledExecutorService scheduledExecutorService,
                         CanaryRetriever canaryRetriever) {

        this.scheduledExecutorService = scheduledExecutorService;
        this.canaryRetriever = canaryRetriever;

        properties.getEndpoints().forEach(this::seedScheduleCanaryCheck);
    }

    public Runnable queryCanaryEndpoint(CanaryEndpoint canaryEndpoint) {

        return () -> {
            try {
                doQuery(canaryEndpoint);
            }
            finally {
                // TODO adjust timing to take into consideration the time it took to fetch the canary
                scheduleCanaryCheck(canaryEndpoint);
            }
        };
    }

    private void scheduleCanaryCheck(CanaryEndpoint canaryEndpoint) {

        scheduledExecutorService.schedule(
                queryCanaryEndpoint(canaryEndpoint),
                canaryEndpoint.getPollFrequencySec(),
                TimeUnit.SECONDS);
    }

    /**
     * used for the initial checks, it's randomised to avoid having all canary checks start (and then tick) at once
     */
    private void seedScheduleCanaryCheck(CanaryEndpoint canaryEndpoint) {

        scheduledExecutorService.schedule(
                queryCanaryEndpoint(canaryEndpoint),
                nextInt(1, 10),
                TimeUnit.SECONDS);
    }

    private void doQuery(CanaryEndpoint canaryEndpoint) {

        logger.info("Querying " + canaryEndpoint.getName());
        canaryRetriever.retrieveAndUpdate(canaryEndpoint);
    }
}
