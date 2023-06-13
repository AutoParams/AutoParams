package autoparams.generator;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new TypeMatchingGenerator(Factories::createBoolean, boolean.class, Boolean.class),
            new IntegerGenerator(),
            new LongGenerator(),
            new ByteGenerator(),
            new ShortGenerator(),
            new FloatGenerator(),
            new DoubleGenerator(),
            new TypeMatchingGenerator(Factories::createChar, char.class, Character.class));
    }

}
