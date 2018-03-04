package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.jcanary.api.Canary;

import java.util.List;

/**
 * @author Martin Bechtle
 */
public interface WarningService {

    List<Warning> getServiceWarnings();

    void onCanaryReceived(Canary canary);
}
