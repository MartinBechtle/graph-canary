package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Martin Bechtle
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void notifyHealthChange(GraphEdge edge) {

        logger.info("Link from [] to [] is now []", edge.getFrom(), edge.getTo(), edge.getDependencyStatus());
    }
}
