package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * In-memory implementation of {@link WarningService}
 * <p>
 * Listens to all retrieved canaries and interacts with {@link EmailService} whenever a dependency becomes unhealthy
 * or goes back to healthy. Also keeps track of all services for which the retrieval of data from the Canary
 * endpoint has failed.
 *
 * @author Martin Bechtle
 */
public class InMemoryDynamicWarningService implements WarningService {

    private final EmailService emailService;

    private final Map<String, Set<String>> unhealthyDependencies;

    private final Map<String, CanaryResult> unhealthyServices;

    public InMemoryDynamicWarningService(EmailService emailService) {

        this.emailService = emailService;
        this.unhealthyDependencies = new ConcurrentHashMap<>();
        this.unhealthyServices = new ConcurrentHashMap<>();
    }

    @Override
    public List<Warning> getServiceWarnings() {

        return unhealthyServices.entrySet().stream()
                .map(entry -> new Warning(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    @Override
    public void onCanaryReceived(Canary canary) {

        String serviceName = canary.getServiceName();
        CanaryResult canaryResult = canary.getResult();

        if (canaryResult != CanaryResult.OK) {

            addUnhealthyService(serviceName, canaryResult);
        } else {

            removeUnhealthyServiceIfPresent(serviceName);

            canary.getTweets().forEach(healthTweet -> {

                Dependency dependency = healthTweet.getDependency();
                HealthResult result = healthTweet.getResult();
                String dependencyName = dependency.getName();
                DependencyStatus dependencyStatus = result.getStatus();

                if (!isEmpty(dependencyName)) {

                    if (dependencyStatus == DependencyStatus.HEALTHY) {
                        removeUnhealthyDependencyIfPresent(dependencyName, serviceName);
                    } else {
                        addUnhealthyDependency(dependencyName, serviceName, dependencyStatus, result.getStatusText());
                    }
                }
            });
        }
    }

    private void addUnhealthyService(String serviceName, CanaryResult result) {

        unhealthyServices.computeIfAbsent(serviceName, key -> {

            emailService.notifyServiceStatusChange(serviceName, result);
            return result;
        });
    }

    private void removeUnhealthyServiceIfPresent(String serviceName) {

        if (unhealthyServices.remove(serviceName) != null) {

            emailService.notifyServiceStatusChange(serviceName, CanaryResult.OK);
        }
    }

    private void removeUnhealthyDependencyIfPresent(String dependencyName, String serviceName) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {
            if (serviceSet != null && serviceSet.remove(serviceName)) {
                // dependency was present in the set, thus unhealthy, now let's notify it's healthy again
                emailService.notifyDependencyHealthChange(
                        new GraphEdge(serviceName, dependencyName, DependencyStatus.HEALTHY, ""));
            }
            return serviceSet;
        });
    }

    private void addUnhealthyDependency(String dependencyName,
                                        String serviceName,
                                        DependencyStatus status,
                                        String statusText) {

        unhealthyDependencies.compute(dependencyName, (key, serviceSet) -> {

            Set<String> servicesPointingToDependency = ofNullable(serviceSet).orElseGet(HashSet::new);
            if (!servicesPointingToDependency.contains(serviceName)) {

                servicesPointingToDependency.add(serviceName);
                emailService.notifyDependencyHealthChange(
                        new GraphEdge(serviceName, dependencyName, status, statusText));
            }
            return servicesPointingToDependency;
        });
    }
}
