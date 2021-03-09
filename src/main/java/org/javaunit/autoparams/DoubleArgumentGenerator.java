package org.javaunit.autoparams;

import java.util.Optional;

final class DoubleArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(ParameterDescriptor parameter, ArgumentGenerationContext context) {
        Class<?> type = parameter.getType();
        return type.equals(double.class) || type.equals(Double.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextDouble());
    }

}
