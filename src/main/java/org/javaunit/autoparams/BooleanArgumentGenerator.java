package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;

final class BooleanArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.equals(boolean.class) || type.equals(Boolean.class) ? factory() : empty;
    }

    private Optional<Object> factory() {
        return Optional.of(random.nextInt() % 2 == 0);
    }
    
}
