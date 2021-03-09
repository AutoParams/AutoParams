package org.javaunit.autoparams;

import java.util.Optional;

final class FloatGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(float.class) || type.equals(Float.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextFloat());
    }

}
