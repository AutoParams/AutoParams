package org.javaunit.autoparams.autofixture.generators;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.shuffle;

public class EnumDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.isEnum();
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        Object[] enums = clazz.getEnumConstants();
        shuffle(Arrays.asList(enums));
        return Optional.of(enums[0]);
    }

}
