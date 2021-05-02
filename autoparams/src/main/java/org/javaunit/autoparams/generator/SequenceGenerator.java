package org.javaunit.autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class SequenceGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return isCollection((Class<?>) type.getRawType())
            ? new ObjectContainer(factory((Class<?>) type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private static boolean isCollection(Class<?> type) {
        return type.equals(ArrayList.class) || type.equals(List.class)
            || type.equals(Collection.class) || type.equals(Iterable.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> factory(
        Class<? extends T> elementType, ObjectGenerationContext context) {
        ArrayList<T> instance = new ArrayList<T>();
        ObjectQuery query = () -> elementType;
        for (int i = 0; i < SIZE; i++) {
            instance.add((T) context.getGenerator().generate(query, context).unwrapOrElseThrow());
        }

        return instance;
    }

}
