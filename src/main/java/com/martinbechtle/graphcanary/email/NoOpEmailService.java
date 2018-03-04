package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.CanaryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No-op implementation of {@link EmailService}
 * <p>
 * Just prints to console. Used when there is no email configuration.
 *
 * @author Martin Bechtle
 */
public class NoOpEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(NoOpEmailService.class);

    @Override
    public void notifyDependencyHealthChange(GraphEdge edge) {

        logger.info("Link from [{}] to [{}] is now [{}]", edge.getFrom(), edge.getTo(), edge.getDependencyStatus());
    }

    @Override
    public void notifyServiceStatusChange(String serviceName, CanaryResult result) {

        if (result == CanaryResult.OK) {
            logger.info("Service [{}] canary status is now OK", serviceName);
        }
        else {
            logger.info("Service [{}] canary endpoint retrieval resulted in [{}]", serviceName, result);
        }
    }
}
