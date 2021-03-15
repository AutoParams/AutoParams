package org.javaunit.autoparams;

import java.util.Optional;

final class BooleanGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(boolean.class) || type.equals(Boolean.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextInt() % 2 == 0);
    }

}
