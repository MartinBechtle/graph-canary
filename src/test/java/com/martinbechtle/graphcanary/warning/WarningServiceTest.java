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
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

/**
 * @author Martin Bechtle
 */
@RunWith(MockitoJUnitRunner.class)
public class WarningServiceTest {

    private static final String SERVICE_NAME = "service";
    private static final String DEPENDENCY_NAME = "dependency";

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
        verify(emailService).notifyDependencyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailOnce_WhenUnhealthyTweetReceivedTwice() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED);

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyDependencyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));

        warningService.onCanaryReceived(degradedCanary);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailAgain_WhenUnhealthyDependencyBecomesHealthy() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED);

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyDependencyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED));

        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY);
        warningService.onCanaryReceived(healthyCanary);
        verify(emailService).notifyDependencyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY));
    }

    @Test
    public void onCanaryReceived_ShouldSendEmail_WhenServiceFailsToReturnCanary() {

        Canary errorCanary = errorCanary(SERVICE_NAME, CanaryResult.ERROR);

        warningService.onCanaryReceived(errorCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.ERROR);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailOnce_WhenServiceFailsToReturnCanaryMultipleTimes() {

        Canary errorCanary = errorCanary(SERVICE_NAME, CanaryResult.ERROR);

        warningService.onCanaryReceived(errorCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.ERROR);

        warningService.onCanaryReceived(errorCanary);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmail_WhenServiceWithFailedCanaryHasOkCanaryNow() {

        Canary errorCanary = errorCanary(SERVICE_NAME, CanaryResult.ERROR);
        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY);

        warningService.onCanaryReceived(errorCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.ERROR);

        warningService.onCanaryReceived(healthyCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.OK);
    }

    private static Canary errorCanary(String serviceName, CanaryResult canaryResult) {

        return new Canary(serviceName, canaryResult, emptyList());
    }

    private static Canary singleTweetCanary(String serviceName, String dependencyName, DependencyStatus status) {

        return new Canary(serviceName, CanaryResult.OK, Collections.singletonList(new HealthTweet(
                new Dependency(DependencyImportance.PRIMARY, DependencyType.API, dependencyName),
                new HealthResult(status, ""), 100
        )));
    }
}