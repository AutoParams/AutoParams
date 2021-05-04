package org.javaunit.autoparams.generator;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new IntegerGenerator(),
            new DoubleGenerator(),
            new LongGenerator(),
            new TypeMatchingGenerator(Factories::createBoolean, boolean.class, Boolean.class),
            new TypeMatchingGenerator(Factories::createByte, byte.class, Byte.class),
            new TypeMatchingGenerator(Factories::createShort, short.class, Short.class),
            new TypeMatchingGenerator(Factories::createFloat, float.class, Float.class),
            new TypeMatchingGenerator(Factories::createChar, char.class, Character.class));
    }

}
