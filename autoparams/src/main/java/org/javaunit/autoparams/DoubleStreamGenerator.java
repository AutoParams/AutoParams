package org.javaunit.autoparams;

import java.util.Optional;
import java.util.stream.DoubleStream;

final class DoubleStreamGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(DoubleStream.class) ? Optional.of(factory()) : Optional.empty();
    }

    private DoubleStream factory() {
        int size = 3;
        return DoubleStream.generate(RANDOM::nextDouble).limit(size);
    }
}
