package org.javaunit.autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

final class ArgumentQuery implements ObjectQuery {

    private final Parameter parameter;

    public ArgumentQuery(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public Type getType() {
        return parameter.getAnnotatedType().getType();
    }

}
