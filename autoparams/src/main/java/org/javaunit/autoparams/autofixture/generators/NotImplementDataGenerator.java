package org.javaunit.autoparams.autofixture.generators;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class NotImplementDataGenerator implements DataGenerator {
    @Override
    public boolean isSupport(Class clazz) {
        return true;
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        throw new NotImplementedException();
    }
}
