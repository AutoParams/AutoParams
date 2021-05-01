package org.javaunit.autoparams.generator;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new ObjectGeneratorGenerator(),
            new PrimitiveValueGenerator(),
            new SimpleValueObjectGenerator(),
            new CollectionGenerator(),
            new ComplexObjectGenerator()
        );
    }

}
