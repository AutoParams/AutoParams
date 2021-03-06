package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;

final class DoubleArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.equals(double.class) || type.equals(Double.class) ? factory() : empty;
    }

    private Optional<Object> factory() {
        return Optional.of(random.nextDouble());
    }

}
