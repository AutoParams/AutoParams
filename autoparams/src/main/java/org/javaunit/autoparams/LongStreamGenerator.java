package org.javaunit.autoparams;

import java.util.Optional;
import java.util.stream.LongStream;

final class LongStreamGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(LongStream.class) ? Optional.of(factory()) : Optional.empty();
    }

    private LongStream factory() {
        int size = 3;
        return LongStream.generate(RANDOM::nextLong).limit(size);
    }
}
