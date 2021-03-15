package org.javaunit.autoparams;

import java.util.Optional;

final class LongGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(long.class) || type.equals(Long.class) ? Optional.of(factory()) : Optional.empty();
    }

    private long factory() {
        return RANDOM.nextLong();
    }

}
