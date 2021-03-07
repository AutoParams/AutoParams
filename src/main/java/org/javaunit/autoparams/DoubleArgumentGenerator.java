package org.javaunit.autoparams;

import java.util.Optional;

final class DoubleArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(ArgumentGenerationContext context) {
        Class<?> type = context.getParameter().getType();
        return type.equals(double.class) || type.equals(Double.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextDouble());
    }

}
