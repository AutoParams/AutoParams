package org.javaunit.autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

final class ParameterQuery implements ObjectQuery {

    private final Parameter parameter;

    public ParameterQuery(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public Type getType() {
        return parameter.getAnnotatedType().getType();
    }

    @Override
    public String toString() {
        return parameter.toString();
    }
}
