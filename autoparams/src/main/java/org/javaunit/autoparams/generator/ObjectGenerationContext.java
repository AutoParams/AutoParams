package org.javaunit.autoparams.generator;

public final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;
    }

    public Object generate(ObjectQuery query) {
        return query.getType().equals(ObjectGenerationContext.class)
            ? this
            : this.generator.generate(query, this).unwrapOrElseThrow();
    }

}
