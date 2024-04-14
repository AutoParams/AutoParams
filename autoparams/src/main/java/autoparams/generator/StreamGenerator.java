package autoparams.generator;

final class StreamGenerator extends CompositeObjectGenerator {

    public StreamGenerator() {
        super(
            new IntStreamGenerator(),
            new LongStreamGenerator(),
            new DoubleStreamGenerator(),
            new GenericStreamGenerator()
        );
    }
}
