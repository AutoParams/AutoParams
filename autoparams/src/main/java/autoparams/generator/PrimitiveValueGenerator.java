package autoparams.generator;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new BooleanGenerator(),
            new CharacterGenerator(),
            new ByteGenerator(),
            new ShortGenerator(),
            new IntegerGenerator(),
            new LongGenerator(),
            new FloatGenerator(),
            new DoubleGenerator()
        );
    }
}
