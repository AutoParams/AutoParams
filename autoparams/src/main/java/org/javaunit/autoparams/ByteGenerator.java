package org.javaunit.autoparams;

import java.util.Optional;

final class ByteGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(byte.class) || type.equals(Byte.class)
            ? Optional.of(factory())
            : Optional.empty();
    }

    private byte factory() {
        return (byte) RANDOM.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
    }

}
