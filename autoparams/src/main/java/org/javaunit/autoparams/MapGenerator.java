package org.javaunit.autoparams;

import java.util.HashMap;
import java.util.Map;

final class MapGenerator extends GenericObjectGenerator {

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class) || type.equals(Map.class);
    }

    private Object factory(GenericObjectQuery query, ObjectGenerationContext context) {
        return factory(getKeyType(query), getValueType(query), context);
    }

    @SuppressWarnings("unchecked")
    private <K, V> HashMap<K, V> factory(
        Class<? extends K> keyType,
        Class<? extends V> valueType,
        ObjectGenerationContext context) {

        HashMap<K, V> instance = new HashMap<K, V>();
        int size = 3;
        ObjectQuery keyQuery = new ObjectQuery(keyType);
        ObjectQuery valueQuery = new ObjectQuery(valueType);
        for (int i = 0; i < size; i++) {
            instance.put(
                (K) context.generate(keyQuery), (V) context.generate(valueQuery));
        }

        return instance;
    }

    private Class<?> getKeyType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    private Class<?> getValueType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[1];
    }

    @Override
    protected GenerationResult generateObject(
        GenericObjectQuery query,
        ObjectGenerationContext context
    ) {
        return isMap(query.getType())
            ? GenerationResult.presence(factory(query, context))
            : GenerationResult.absence();
    }

}
