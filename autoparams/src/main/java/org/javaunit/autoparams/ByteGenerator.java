package org.javaunit.autoparams;

import java.util.Optional;

final class ByteGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(byte.class) || type.equals(Byte.class)
            ? GenerationResult.presence(factory())
            : GenerationResult.absence();
    }

    private byte factory() {
        return (byte) RANDOM.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
    }

}
