package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.Dependency;
import com.martinbechtle.jcanary.api.DependencyStatus;
import com.martinbechtle.jcanary.api.HealthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Martin Bechtle
 */
@Service
public class WarningService {

    private final EmailService emailService;

    private final Map<String, Set<String>> unhealthyDependencies;

    @Autowired
    public WarningService(EmailService emailService) {

        this.emailService = emailService;
        this.unhealthyDependencies = new ConcurrentHashMap<>();
    }

    public void onCanaryReceived(Canary canary) {

        String serviceName = canary.getServiceName();
        canary.getTweets().forEach(healthTweet -> {

            Dependency dependency = healthTweet.getDependency();
            HealthResult result = healthTweet.getResult();
            String dependencyName = dependency.getName();

            if (!isEmpty(dependencyName)) {

                if (result.getStatus() == DependencyStatus.HEALTHY) {
                    removeIfPresent(serviceName, dependency.getName());
                }
                else {
                    addUnhealthyDependency(dependencyName, serviceName, result.getStatus());
                }
            }
        });
    }

    private void removeIfPresent(String dependencyName, String serviceName) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {
            if (serviceSet != null) {
                serviceSet.remove(serviceName);
                emailService.notifyHealthChange(new GraphEdge(serviceName, dependencyName, DependencyStatus.HEALTHY));
            }
            return serviceSet;
        });
    }

    private void addUnhealthyDependency(String dependencyName, String serviceName, DependencyStatus status) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {

            Set<String> servicesPointingToDependency = ofNullable(serviceSet).orElseGet(HashSet::new);
            if (!servicesPointingToDependency.contains(serviceName)) {
                servicesPointingToDependency.add(serviceName);
                emailService.notifyHealthChange(new GraphEdge(serviceName, dependencyName, status));
            }
            return servicesPointingToDependency;
        });
    }
}
