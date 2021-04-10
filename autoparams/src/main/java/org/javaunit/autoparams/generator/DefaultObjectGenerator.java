package org.javaunit.autoparams.generator;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new PrimitiveValueGenerator()
        );
    }

}
