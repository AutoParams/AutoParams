package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;

final class IntegerArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.equals(int.class) || type.equals(Integer.class) ? factory() : empty;
    }

    private Optional<Object> factory() {
        return Optional.of(random.nextInt());
    }

}
