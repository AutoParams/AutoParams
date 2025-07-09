package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static autoparams.generator.CollectionGenerator.getSize;

final class MapGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            if (isMap((Class<?>) type.getRawType())) {
                int size = getSize(query);
                return new ObjectContainer(generateMap(type, size, context));
            }
        }

        return ObjectContainer.EMPTY;
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class)
            || type.equals(Map.class)
            || type.equals(AbstractMap.class);
    }

    private static HashMap<Object, Object> generateMap(
        ParameterizedType mapType,
        int size,
        ResolutionContext context
    ) {
        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(mapType);
        Type keyType = mapType.getActualTypeArguments()[0];
        Type resolvedKeyType = typeResolver.resolve(keyType);
        ObjectQuery keyQuery = new DefaultObjectQuery(resolvedKeyType);

        Type valueType = mapType.getActualTypeArguments()[1];
        Type resolvedValueType = typeResolver.resolve(valueType);
        ObjectQuery valueQuery = new DefaultObjectQuery(resolvedValueType);

        HashMap<Object, Object> instance = new HashMap<>();
        for (int i = 0; i < size; i++) {
            instance.put(
                context.resolve(keyQuery),
                context.resolve(valueQuery)
            );
        }

        return instance;
    }
}
