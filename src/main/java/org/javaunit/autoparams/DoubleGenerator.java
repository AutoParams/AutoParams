package org.javaunit.autoparams;

import java.util.Optional;

final class DoubleGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(double.class) || type.equals(Double.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextDouble());
    }

}
