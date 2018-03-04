package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static com.martinbechtle.jcanary.api.DependencyStatus.DEGRADED;
import static com.martinbechtle.jcanary.api.DependencyStatus.HEALTHY;
import static org.mockito.Mockito.*;

/**
 * @author Martin Bechtle
 */
@RunWith(MockitoJUnitRunner.class)
public class WarningServiceTest {

    private static final String SERVICE_NAME = "service";
    private static final String DEPENDENCY_NAME = "dependency";

    // TODO implement warnings for canaries that failed to retrieve
    // TODO how are failed canaries treated in UI?

    @InjectMocks
    private WarningService warningService;

    @Mock
    private EmailService emailService;

    @Test
    public void onCanaryReceived_ShouldNotSendEmail_WhenHealthyTweetReceived_AndDependencyWasAlreadyHealthy() {

        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY);

        warningService.onCanaryReceived(healthyCanary);
        verifyZeroInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmail_WhenUnhealthyTweetReceived_AndDependencyWasPreviouslyHealthy() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED);

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailOnce_WhenUnhealthyTweetReceivedTwice() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED);

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));

        warningService.onCanaryReceived(degradedCanary);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailAgain_WhenUnhealthyDependencyBecomesHealthy() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED);

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));

        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY);
        warningService.onCanaryReceived(healthyCanary);
        verify(emailService).notifyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY));
    }

    private static Canary singleTweetCanary(String serviceName, String dependencyName, DependencyStatus status) {

        return new Canary(serviceName, CanaryResult.OK, Collections.singletonList(new HealthTweet(
                new Dependency(DependencyImportance.PRIMARY, DependencyType.API, dependencyName),
                new HealthResult(status, ""), 100
        )));
    }
}