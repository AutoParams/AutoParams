package org.javaunit.autoparams;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

final class GenericStreamGenerator extends GenericObjectGenerator {

    private static Class<?> getComponentType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    @Override
    protected Optional<Object> generate(GenericObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(Stream.class) ? Optional.of(factory(getComponentType(query), context))
            : Optional.empty();
    }

    private <T> Stream<T> factory(
        Class<? extends T> componentType,
        ObjectGenerationContext context
    ) {
        ArrayList<T> list = CollectionGenerator.factory(componentType, context);
        return list.stream();
    }

}
