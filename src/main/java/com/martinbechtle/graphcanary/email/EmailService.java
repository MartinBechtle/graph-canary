package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.CanaryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Martin Bechtle
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void notifyDependencyHealthChange(GraphEdge edge) {

        logger.info("Link from [{}] to [{}] is now [{}]", edge.getFrom(), edge.getTo(), edge.getDependencyStatus());
    }

    public void notifyServiceStatusChange(String serviceName, CanaryResult result) {

        if (result == CanaryResult.OK) {
            logger.info("Service [{}] canary status is now OK");
        }
        else {
            logger.info("Service [{}] canary endpoint retrieval resulted in [{}]", result);
        }
    }
}
