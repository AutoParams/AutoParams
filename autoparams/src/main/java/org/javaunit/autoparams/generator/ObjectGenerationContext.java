package org.javaunit.autoparams.generator;

public final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;
    }

    ObjectGenerator getGenerator() {
        return generator;
    }

}
