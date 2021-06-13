package org.javaunit.autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

final class MapGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return isMap((Class<?>) type.getRawType())
            ? new ObjectContainer(factory(
                type.getActualTypeArguments()[0],
                type.getActualTypeArguments()[1],
                context))
            : ObjectContainer.EMPTY;
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class) || type.equals(Map.class);
    }

    @SuppressWarnings("unchecked")
    private <K, V> HashMap<K, V> factory(
        Type keyType,
        Type valueType,
        ObjectGenerationContext context
    ) {
        HashMap<K, V> instance = new HashMap<K, V>();

        ObjectQuery keyQuery = () -> keyType;
        ObjectQuery valueQuery = () -> valueType;

        for (int i = 0; i < SIZE; i++) {
            instance.put(
                (K) context.getGenerator().generate(keyQuery, context).unwrapOrElseThrow(),
                (V) context.getGenerator().generate(valueQuery, context).unwrapOrElseThrow());
        }

        return instance;
    }

}
