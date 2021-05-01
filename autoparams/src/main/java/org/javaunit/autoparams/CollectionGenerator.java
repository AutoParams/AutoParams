package org.javaunit.autoparams;

final class CollectionGenerator extends CompositeObjectGenerator {

    public CollectionGenerator() {
        super(
            new SequenceGenerator(),
            new MapGenerator(),
            new SetGenerator());
    }

}
