package com.martinbechtle.graphcanary.email;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Duration;

/**
 * Unit test for {@link StartupClock}
 *
 * @author martin
 */
@RunWith(MockitoJUnitRunner.class)
public class StartupClockTest {

    @Mock
    private Clock clock;

    @Test
    public void timeHasPassedSinceStartup() throws Exception {

        timeIs(1000);
        StartupClock startupClock = new StartupClock(clock);

        timeIs(2000);
        boolean oneSecondPassed = startupClock.timeHasPassedSinceStartup(Duration.ofSeconds(1));
        boolean twoSecondsPassed = startupClock.timeHasPassedSinceStartup(Duration.ofSeconds(2));
        Assertions.assertThat(oneSecondPassed).isTrue();
        Assertions.assertThat(twoSecondsPassed).isFalse();
    }

    private void timeIs(long millis) {

        Mockito.reset(clock);
        Mockito.when(clock.millis()).thenReturn(millis);
    }

}