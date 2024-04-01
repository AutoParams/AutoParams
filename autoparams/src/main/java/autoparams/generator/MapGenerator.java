package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import autoparams.ResolutionContext;

final class MapGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ResolutionContext context) {
        return isMap((Class<?>) type.getRawType())
            ? new ObjectContainer(factory(
            type.getActualTypeArguments()[0],
            type.getActualTypeArguments()[1],
            context
        ))
            : ObjectContainer.EMPTY;
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class)
            || type.equals(Map.class)
            || type.equals(AbstractMap.class);
    }

    @SuppressWarnings("unchecked")
    private <K, V> HashMap<K, V> factory(
        Type keyType,
        Type valueType,
        ResolutionContext context
    ) {
        HashMap<K, V> instance = new HashMap<>();

        for (int i = 0; i < SIZE; i++) {
            instance.put(
                (K) context.resolve(ObjectQuery.fromType(keyType)),
                (V) context.resolve(ObjectQuery.fromType(valueType))
            );
        }

        return instance;
    }
}
