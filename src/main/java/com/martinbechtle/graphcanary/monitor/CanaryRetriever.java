package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.jcanary.api.Canary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Retrieves a {@link Canary} from a {@link CanaryEndpoint} and updates the {@link GraphService} with the new data.
 * Thread safe.
 *
 * @author Martin Bechtle
 */
@Component
public class CanaryRetriever {

    private static final Logger logger = LoggerFactory.getLogger(CanaryRetriever.class );

    private final GraphService graphService;

    private final CanaryHttpClient canaryHttpClient;

    public CanaryRetriever(GraphService graphService, CanaryHttpClient canaryHttpClient) {

        this.graphService = graphService;

        this.canaryHttpClient = canaryHttpClient;
    }

    public void retrieveAndUpdate(CanaryEndpoint canaryEndpoint) {

        try {
            Canary canary =  canaryHttpClient.getCanary(canaryEndpoint);
            graphService.onCanaryReceived(canary);
        }
        // TODO insert some error in the graph
        catch (IOException e) {
            logger.warn("Network error while retrieving canary for " + canaryEndpoint.getName(), e);
        }
        catch (RuntimeException e) {
            logger.warn("Http error while retrieving canary for " + canaryEndpoint.getName(), e);
        }
    }
}
