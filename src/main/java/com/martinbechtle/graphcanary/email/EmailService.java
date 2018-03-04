package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.CanaryResult;

/**
 * @author Martin Bechtle
 */
public interface EmailService {

    void notifyDependencyHealthChange(GraphEdge edge);

    void notifyServiceStatusChange(String serviceName, CanaryResult result);
}
