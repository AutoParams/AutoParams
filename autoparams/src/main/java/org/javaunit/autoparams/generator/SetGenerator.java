package org.javaunit.autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Set;

final class SetGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return isSet((Class<?>) type.getRawType())
            ? new ObjectContainer(factory(type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> factory(
        Type elementType,
        ObjectGenerationContext context
    ) {
        HashSet<T> instance = new HashSet<>();
        ObjectQuery query = () -> elementType;
        for (int i = 0; i < SIZE; i++) {
            instance.add((T) context.getGenerator().generate(query, context).unwrapOrElseThrow());
        }

        return instance;
    }

}
