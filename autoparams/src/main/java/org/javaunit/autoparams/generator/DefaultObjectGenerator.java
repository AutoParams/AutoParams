package org.javaunit.autoparams.generator;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new ObjectGenerationContextGenerator(),
            new PrimitiveValueGenerator(),
            new SimpleValueObjectGenerator(),
            new CollectionGenerator(),
            new StreamGenerator(),
            new ComplexObjectGenerator()
        );
    }

}
