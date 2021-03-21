package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.Random;

public interface DataGenerator {
    Random RANDOM = new Random();
    boolean isSupport(Class clazz);
    // TODO refactor 2nd parameter
    Optional<Object> generate(Class clazz, ParameterizedType parameterizedType);
}
