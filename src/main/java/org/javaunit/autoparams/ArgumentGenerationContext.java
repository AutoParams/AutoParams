package org.javaunit.autoparams;

final class ArgumentGenerationContext {

    private final ArgumentGenerator generator;

    public ArgumentGenerationContext(ArgumentGenerator generator) {
        this.generator = generator;

    }

    public ArgumentGenerator getGenerator() {
        return generator;
    }

}
