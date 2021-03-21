package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.UUID;

public final class StringDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.equals(String.class);
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        return Optional.of(UUID.randomUUID().toString());
    }
}
