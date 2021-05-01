package org.javaunit.autoparams.generator;

final class CollectionGenerator extends CompositeObjectGenerator {

    public CollectionGenerator() {
        super(new ArrayGenerator());
    }

}
