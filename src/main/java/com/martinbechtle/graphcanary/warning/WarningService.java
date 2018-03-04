package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.*;
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

    private final Set<String> unhealthyServices;

    @Autowired
    public WarningService(EmailService emailService) {

        this.emailService = emailService;
        this.unhealthyDependencies = new ConcurrentHashMap<>();
        this.unhealthyServices = new HashSet<>();
    }

    public void onCanaryReceived(Canary canary) {

        String serviceName = canary.getServiceName();
        CanaryResult canaryResult = canary.getResult();

        if (canaryResult != CanaryResult.OK) {

            addUnhealthyService(serviceName, canaryResult);
        }
        else {

            removeUnhealthyServiceIfPresent(serviceName);

            canary.getTweets().forEach(healthTweet -> {

                Dependency dependency = healthTweet.getDependency();
                HealthResult result = healthTweet.getResult();
                String dependencyName = dependency.getName();
                DependencyStatus dependencyStatus = result.getStatus();

                if (!isEmpty(dependencyName)) {

                    if (dependencyStatus == DependencyStatus.HEALTHY) {
                        removeUnhealthyDependencyIfPresent(serviceName, dependencyName);
                    } else {
                        addUnhealthyDependency(dependencyName, serviceName, dependencyStatus);
                    }
                }
            });
        }
    }

    private void addUnhealthyService(String serviceName, CanaryResult result) {

        if (!unhealthyServices.contains(serviceName)) {

            unhealthyServices.add(serviceName);
            emailService.notifyServiceStatusChange(serviceName, result);
        }
    }

    private void removeUnhealthyServiceIfPresent(String serviceName) {

        if (unhealthyServices.contains(serviceName)) {

            unhealthyServices.remove(serviceName);
            emailService.notifyServiceStatusChange(serviceName, CanaryResult.OK);
        }
    }

    private void removeUnhealthyDependencyIfPresent(String dependencyName, String serviceName) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {
            if (serviceSet != null) {
                serviceSet.remove(serviceName);
                emailService.notifyDependencyHealthChange(new GraphEdge(serviceName, dependencyName, DependencyStatus.HEALTHY));
            }
            return serviceSet;
        });
    }

    private void addUnhealthyDependency(String dependencyName, String serviceName, DependencyStatus status) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {

            Set<String> servicesPointingToDependency = ofNullable(serviceSet).orElseGet(HashSet::new);
            if (!servicesPointingToDependency.contains(serviceName)) {
                servicesPointingToDependency.add(serviceName);
                emailService.notifyDependencyHealthChange(new GraphEdge(serviceName, dependencyName, status));
            }
            return servicesPointingToDependency;
        });
    }
}
