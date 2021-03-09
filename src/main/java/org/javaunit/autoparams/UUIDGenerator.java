package org.javaunit.autoparams;

import java.util.Optional;
import java.util.UUID;

final class UUIDGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(UUID.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(UUID.randomUUID());
    }

}
