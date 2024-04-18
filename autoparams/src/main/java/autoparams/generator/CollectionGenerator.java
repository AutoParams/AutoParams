package autoparams.generator;

final class CollectionGenerator extends CompositeObjectGenerator {

    public CollectionGenerator() {
        super(
            new ArrayGenerator(),
            new SequenceGenerator(),
            new MapGenerator(),
            new SetGenerator()
        );
    }
}
