package org.javaunit.autoparams;

import java.util.Optional;

final class ShortGenerator implements ObjectGenerator {

    private static short factory() {
        return (short) RANDOM.nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(short.class) || type.equals(Short.class)
               ? Optional.of(factory())
               : Optional.empty();
    }
}
