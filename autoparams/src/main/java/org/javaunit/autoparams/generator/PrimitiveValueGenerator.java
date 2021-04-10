package org.javaunit.autoparams.generator;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new IntegerGenerator()
        );
    }

}
