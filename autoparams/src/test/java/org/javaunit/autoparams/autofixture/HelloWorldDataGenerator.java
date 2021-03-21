package org.javaunit.autoparams.autofixture;


import org.javaunit.autoparams.autofixture.generators.DataGenerator;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

class HelloWorldDataGenerator implements DataGenerator {
    public HelloWorldDataGenerator() {}

    @Override
    public boolean isSupport(Class clazz) {
        return true;
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        return Optional.of("hello world");
    }
}

