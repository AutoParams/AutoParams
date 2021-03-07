package org.javaunit.autoparams;

import java.lang.reflect.Parameter;

final class ArgumentGenerationContext {

    private final Parameter parameter;
    private final ArgumentGenerator generator;

    public ArgumentGenerationContext(Parameter parameter, ArgumentGenerator generator) {
        this.parameter = parameter;
        this.generator = generator;

    }

    public Parameter getParameter() {
        return parameter;
    }

    public ArgumentGenerator getGenerator() {
        return generator;
    }

}
