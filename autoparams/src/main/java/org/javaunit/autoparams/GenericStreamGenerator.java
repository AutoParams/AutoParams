package org.javaunit.autoparams;

import java.util.ArrayList;
import java.util.stream.Stream;

final class GenericStreamGenerator extends GenericObjectGenerator {

    private static Class<?> getComponentType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    private <T> Stream<T> factory(
        Class<? extends T> componentType,
        ObjectGenerationContext context
    ) {
        ArrayList<T> list = SequenceGenerator.factory(componentType, context);
        return list.stream();
    }

    @Override
    protected GenerationResult generate(GenericObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(Stream.class)
            ? GenerationResult.presence(factory(getComponentType(query), context))
            : GenerationResult.absence();
    }

}
