package autoparams.generator;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new TypeMatchingGenerator(
                Factories::createBoolean,
                boolean.class,
                Boolean.class),
            new TypeMatchingGenerator(
                Factories::createChar,
                char.class,
                Character.class),
            new ByteGenerator(),
            new ShortGenerator(),
            new IntegerGenerator(),
            new LongGenerator(),
            new FloatGenerator(),
            new DoubleGenerator());
    }
}
