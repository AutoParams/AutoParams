package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSizeSupplier;

final class MapGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            return generate(type, getSizeSupplier(query), context);
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private ObjectContainer generate(
        ParameterizedType type,
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        return isMap((Class<?>) type.getRawType())
            ? new ObjectContainer(generateMap(type, sizeSupplier, context)) :
            ObjectContainer.EMPTY;
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class)
            || type.equals(Map.class)
            || type.equals(AbstractMap.class);
    }

    private static HashMap<Object, Object> generateMap(
        ParameterizedType mapType,
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        Type keyType = mapType.getActualTypeArguments()[0];
        ObjectQuery keyQuery = new DefaultObjectQuery(keyType);

        Type valueType = mapType.getActualTypeArguments()[1];
        ObjectQuery valueQuery = new DefaultObjectQuery(valueType);

        HashMap<Object, Object> instance = new HashMap<>();
        int size = sizeSupplier.getAsInt();
        for (int i = 0; i < size; i++) {
            instance.put(
                context.resolve(keyQuery),
                context.resolve(valueQuery)
            );
        }

        return instance;
    }
}
