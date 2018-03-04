package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.jcanary.api.Canary;
import com.martinbechtle.jcanary.api.CanaryResult;

import java.util.Arrays;
import java.util.List;

/**
 * Static implementation of {@link WarningService} with fake data.
 *
 * @author Martin Bechtle
 */
public class StaticWarningService implements WarningService {

    @Override
    public List<Warning> getServiceWarnings() {

        return Arrays.asList(
                new Warning("failed-service-1", CanaryResult.ERROR),
                new Warning("failed-service-2", CanaryResult.FORBIDDEN)
        );
    }

    @Override
    public void onCanaryReceived(Canary canary) {

        // do nothing
    }
}
