package org.javaunit.autoparams;

import java.util.Optional;
import java.util.UUID;

final class FixedLengthLongSixGenerator implements ObjectGenerator {

    public FixedLengthLongSixGenerator() {
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(long.class) || type.equals(Long.class) ? Optional.of(factory()) : Optional.empty();
    }

    private long factory() {
        return 100000 + RANDOM.nextLong(900000);
    }

}
