package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public final class BooleanDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        return Optional.of(RANDOM.nextInt() % 2 == 0);
    }
}
