package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class MapGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(
        ParameterizedType type,
        ResolutionContext context
    ) {
        return isMap((Class<?>) type.getRawType())
            ? new ObjectContainer(generateMap(type, context))
            : ObjectContainer.EMPTY;
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class)
            || type.equals(Map.class)
            || type.equals(AbstractMap.class);
    }

    private HashMap<Object, Object> generateMap(
        ParameterizedType mapType,
        ResolutionContext context
    ) {
        Type keyType = mapType.getActualTypeArguments()[0];
        ObjectQuery keyQuery = new DefaultObjectQuery(keyType);

        Type valueType = mapType.getActualTypeArguments()[1];
        ObjectQuery valueQuery = new DefaultObjectQuery(valueType);

        HashMap<Object, Object> instance = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            instance.put(
                context.resolve(keyQuery),
                context.resolve(valueQuery)
            );
        }

        return instance;
    }
}
