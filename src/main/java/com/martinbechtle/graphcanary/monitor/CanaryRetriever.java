package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.warning.WarningService;
import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.CanaryResult;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Collections.emptyList;

/**
 * Retrieves a {@link Canary} from a {@link CanaryEndpoint} and updates the {@link GraphService} with the new data.
 * Thread safe.
 *
 * @author Martin Bechtle
 */
@Component
public class CanaryRetriever {

    private static final Logger logger = LoggerFactory.getLogger(CanaryRetriever.class);

    private final GraphService graphService;

    private final WarningService warningService;

    private final CanaryHttpClient canaryHttpClient;

    public CanaryRetriever(GraphService graphService,
                           WarningService warningService,
                           CanaryHttpClient canaryHttpClient) {

        this.graphService = graphService;
        this.warningService = warningService;
        this.canaryHttpClient = canaryHttpClient;
    }

    public void retrieveAndUpdate(CanaryEndpoint canaryEndpoint) {

        String serviceName = canaryEndpoint.getName();

        Canary canary = Try.of(() -> canaryHttpClient.getCanary(canaryEndpoint))
                .getOrElseGet(throwable -> {
                    logger.warn("Http error while retrieving canary for " + canaryEndpoint.getName(), throwable);
                    return new Canary(serviceName, CanaryResult.ERROR, emptyList());
                });

        Try.run(() -> graphService.onCanaryReceived(canary));
        Try.run(() -> warningService.onCanaryReceived(canary));
    }
}
