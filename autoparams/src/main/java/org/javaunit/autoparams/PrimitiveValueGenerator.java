package org.javaunit.autoparams;

final class PrimitiveValueGenerator extends CompositeObjectGenerator {

    public PrimitiveValueGenerator() {
        super(
            new TypeMatchingGenerator(Factories::createBoolean, boolean.class, Boolean.class),
            new TypeMatchingGenerator(Factories::createByte, byte.class, Byte.class),
            new TypeMatchingGenerator(Factories::createShort, short.class, Short.class),
            new TypeMatchingGenerator(Factories::createLong, long.class, Long.class),
            new TypeMatchingGenerator(Factories::createFloat, float.class, Float.class),
            new TypeMatchingGenerator(Factories::createDouble, double.class, Double.class),
            new TypeMatchingGenerator(Factories::createChar, char.class, Character.class));
    }

}
