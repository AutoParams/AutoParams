package org.javaunit.autoparams;

final class CollectionGenerator extends CompositeObjectGenerator {

    public CollectionGenerator() {
        super(
            new SetGenerator());
    }

}
