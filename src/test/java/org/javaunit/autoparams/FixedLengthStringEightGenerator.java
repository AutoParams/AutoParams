package org.javaunit.autoparams;

import java.util.Optional;
import java.util.UUID;

final class FixedLengthStringEightGenerator implements ObjectGenerator {

    public FixedLengthStringEightGenerator() {
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(String.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(UUID.randomUUID().toString().substring(0, 8));
    }

}
