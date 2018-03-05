package com.martinbechtle.graphcanary.email;

import java.time.Clock;
import java.time.Duration;

/**
 * @author martin
 */
public class StartupClock {

    private final long startupTime;

    private final Clock clock;

    public StartupClock(Clock clock) {

        this.startupTime = clock.millis();
        this.clock = clock;
    }

    public long getStartupTimeMillis() {

        return startupTime;
    }

    public boolean timeHasPassedSinceStartup(Duration duration) {

        long nowMs = clock.millis();

        return nowMs - startupTime >= duration.toMillis();
    }
}
