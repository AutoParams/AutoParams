package org.javaunit.autoparams;

import java.lang.reflect.Parameter;

final class ParameterDescriptor {

    private final Class<?> type;

    public ParameterDescriptor(Class<?> type) {
        this.type = type;
    }

    public static ParameterDescriptor create(Parameter parameter) {
        return new ParameterDescriptor(parameter.getType());
    }

    public Class<?> getType() {
        return type;
    }

}
