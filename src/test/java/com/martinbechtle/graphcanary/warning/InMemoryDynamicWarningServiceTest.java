package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static com.martinbechtle.jcanary.api.DependencyStatus.DEGRADED;
import static com.martinbechtle.jcanary.api.DependencyStatus.HEALTHY;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Martin Bechtle
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryDynamicWarningServiceTest {

    private static final String SERVICE_NAME = "service";
    private static final String DEPENDENCY_NAME = "dependency";

    @InjectMocks
    private InMemoryDynamicWarningService warningService;

    @Mock
    private EmailService emailService;

    @Test
    public void onCanaryReceived_ShouldNotSendEmail_WhenHealthyTweetReceived_AndDependencyWasAlreadyHealthy() {

        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY, "");

        warningService.onCanaryReceived(healthyCanary);
        verifyZeroInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmail_WhenUnhealthyTweetReceived_AndDependencyWasPreviouslyHealthy() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText");

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyDependencyHealthChange(
                new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText"));
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailOnce_WhenUnhealthyTweetReceivedTwice() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText");

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyDependencyHealthChange(
                new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText"));

        warningService.onCanaryReceived(degradedCanary);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void onCanaryReceived_ShouldSendEmailAgain_WhenUnhealthyDependencyBecomesHealthy() {

        Canary degradedCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText");

        warningService.onCanaryReceived(degradedCanary);
        verify(emailService).notifyDependencyHealthChange(
                new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, DEGRADED, "statusText"));

        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY, "");
        warningService.onCanaryReceived(healthyCanary);
        verify(emailService).notifyDependencyHealthChange(new GraphEdge(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY, ""));
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
        Canary healthyCanary = singleTweetCanary(SERVICE_NAME, DEPENDENCY_NAME, HEALTHY, "");

        warningService.onCanaryReceived(errorCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.ERROR);

        warningService.onCanaryReceived(healthyCanary);
        verify(emailService).notifyServiceStatusChange(SERVICE_NAME, CanaryResult.OK);
    }

    @Test
    public void getServiceWarnings_ShouldReturnWarningsForAllServicesThatDidNotSucceedRetrievingCanary() {

        Canary service1Error = errorCanary("service1", CanaryResult.ERROR);
        Canary service2Error = errorCanary("service2", CanaryResult.FORBIDDEN);
        Canary service3Error = errorCanary("service3", CanaryResult.ERROR);
        Canary service3Ok = errorCanary("service3", CanaryResult.OK);

        warningService.onCanaryReceived(service1Error);
        warningService.onCanaryReceived(service2Error);
        warningService.onCanaryReceived(service3Error);
        warningService.onCanaryReceived(service3Ok);

        List<Warning> warnings = warningService.getServiceWarnings();
        Set<Warning> expectedWarnings = new HashSet<>(Arrays.asList(
                new Warning("service1", CanaryResult.ERROR),
                new Warning("service2", CanaryResult.FORBIDDEN)
        ));
        // using hashset to avoid failures from order of elements in the list
        assertThat(new HashSet<>(warnings)).isEqualTo(expectedWarnings);
    }

    private static Canary errorCanary(String serviceName, CanaryResult canaryResult) {

        return new Canary(serviceName, canaryResult, emptyList());
    }

    private static Canary singleTweetCanary(String serviceName,
                                            String dependencyName,
                                            DependencyStatus status,
                                            String statusText) {

        return new Canary(serviceName, CanaryResult.OK, Collections.singletonList(new HealthTweet(
                new Dependency(DependencyImportance.PRIMARY, DependencyType.API, dependencyName),
                new HealthResult(status, statusText), 100
        )));
    }
}