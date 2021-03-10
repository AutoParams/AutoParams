package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class StreamGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(Stream.class) ? Optional.of(factory(getComponentType(query), context)) : Optional.empty();
    }

    private Class<?> getComponentType(ObjectQuery query) {
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    private <T> Stream<T> factory(Class<? extends T> componentType, ObjectGenerationContext context) {
        ArrayList<T> list = CollectionGenerator.factory(componentType, context);
        return StreamSupport.stream(list.spliterator(), false);
    }

}
