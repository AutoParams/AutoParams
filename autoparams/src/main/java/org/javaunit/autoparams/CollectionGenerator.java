package org.javaunit.autoparams;

final class CollectionGenerator extends CompositeObjectGenerator {

    public CollectionGenerator() {
        super(
            new ArrayGenerator(),
            new SequenceGenerator(),
            new MapGenerator(),
            new SetGenerator());
    }

}
