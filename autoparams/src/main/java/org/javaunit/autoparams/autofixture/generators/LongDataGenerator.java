package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class LongDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.equals(long.class) || clazz.equals(Long.class);
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        return Optional.of(RANDOM.nextLong());
    }

}
