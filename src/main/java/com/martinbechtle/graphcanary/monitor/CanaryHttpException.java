package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.jcanary.api.Canary;

import java.io.IOException;

/**
 * Thrown in case there is a network error when trying to retrieve a {@link Canary} from a {@link CanaryEndpoint}
 *
 * @author Martin Bechtle
 */
public class CanaryHttpException extends RuntimeException {

    public CanaryHttpException(IOException e) {

        super(e);
    }

    public CanaryHttpException(String msg) {

        super(msg);
    }
}
