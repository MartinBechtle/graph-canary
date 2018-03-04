package com.martinbechtle.graphcanary.warning;

import com.martinbechtle.jcanary.api.CanaryResult;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Martin Bechtle
 */
public class Warning {

    private String serviceName;

    private CanaryResult canaryResult;

    public Warning(String serviceName, CanaryResult canaryResult) {

        this.serviceName = serviceName;
        this.canaryResult = canaryResult;
    }

    public Warning() {

        // for serialisation/deserialisation
    }

    public String getServiceName() {

        return serviceName;
    }

    public Warning setServiceName(String serviceName) {

        this.serviceName = serviceName;
        return this;
    }

    public CanaryResult getCanaryResult() {

        return canaryResult;
    }

    public Warning setCanaryResult(CanaryResult canaryResult) {

        this.canaryResult = canaryResult;
        return this;
    }

    @Override
    public String toString() {

        return "Warning{" +
                "serviceName='" + serviceName + '\'' +
                ", canaryResult=" + canaryResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Warning)) {
            return false;
        }

        Warning warning = (Warning) o;

        return new EqualsBuilder()
                .append(serviceName, warning.serviceName)
                .append(canaryResult, warning.canaryResult)
                .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
                .append(serviceName)
                .append(canaryResult)
                .toHashCode();
    }
}
