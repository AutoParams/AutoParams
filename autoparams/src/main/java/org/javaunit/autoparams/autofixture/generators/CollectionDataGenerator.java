package org.javaunit.autoparams.autofixture.generators;

import org.javaunit.autoparams.autofixture.BasicSupportGenerators;
import org.javaunit.autoparams.autofixture.ParameterizedTypeConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class CollectionDataGenerator implements DataGenerator {

    @Override
    public boolean isSupport(Class clazz) {
        return clazz.equals(Collection.class) || Arrays.asList(clazz.getInterfaces()).contains(Collection.class);
    }

    @Override
    public Optional<Object> generate(Class clazz, ParameterizedType parameterizedType) {
        Type argType = parameterizedType.getActualTypeArguments()[0];
        Object o = BasicSupportGenerators.dataGenerators.stream()
            .filter(x -> x.isSupport((Class)argType))
            .map(x -> x.generate((Class)argType, ParameterizedTypeConverter.convert(argType)))
            .findFirst()
            .orElse(Optional.empty())
            .orElse(null);
        return Optional.of(Arrays.asList(o));
    }

}
