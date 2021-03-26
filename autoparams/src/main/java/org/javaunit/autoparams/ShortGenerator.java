package org.javaunit.autoparams;

import java.util.Optional;

final class ShortGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(short.class) || type.equals(Short.class)
            ? GenerationResult.presence(factory())
            : GenerationResult.absence();
    }

    private static short factory() {
        return (short) RANDOM.nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);
    }
}
