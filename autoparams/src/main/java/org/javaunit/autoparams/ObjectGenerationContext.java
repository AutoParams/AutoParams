package org.javaunit.autoparams;

final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;

    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Object generate(ObjectQuery query) {
        // This generate method always assumes that it can create values with a given query.
        return generator.generate(query, this).get();
    }
}
