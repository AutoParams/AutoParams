package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;

final class FloatArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter, ArgumentGenerationContext context) {
        Class<?> type = parameter.getType();
        return type.equals(float.class) || type.equals(Float.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextFloat());
    }

}
