package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.UUID;

final class StringArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter, ArgumentGenerationContext context) {
        Class<?> type = parameter.getType();
        return type.equals(String.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(UUID.randomUUID().toString());
    }

}
