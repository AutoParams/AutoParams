package org.javaunit.autoparams;

final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;

    }

    public ObjectGenerator getGenerator() {
        return generator;
    }

}
