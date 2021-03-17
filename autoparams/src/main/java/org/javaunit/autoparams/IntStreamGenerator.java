package org.javaunit.autoparams;

import java.util.Optional;
import java.util.stream.IntStream;

public class IntStreamGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(IntStream.class) ? Optional.of(factory()) : Optional.empty();
    }

    private IntStream factory() {
        int size = 3;
        return IntStream.generate(RANDOM::nextInt).distinct().limit(size);
    }
}
