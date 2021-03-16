package org.javaunit.autoparams;

import java.util.Optional;

final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;

    }

    public Optional<Object> generate(ObjectQuery query) {
        return generator.generate(query, this);
    }

}
