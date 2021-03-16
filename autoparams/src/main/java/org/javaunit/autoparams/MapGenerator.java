package org.javaunit.autoparams;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class MapGenerator extends GenericObjectGenerator {

    @Override
    protected Optional<Object> generate(GenericObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isMap(type) ? Optional.of(factory(query, context)) : Optional.empty();
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class) || type.equals(Map.class);
    }

    private Object factory(GenericObjectQuery query, ObjectGenerationContext context) {
        return factory(getKeyType(query), getValueType(query), context);
    }

    @SuppressWarnings("unchecked")
    private <K, V> HashMap<K, V> factory(Class<? extends K> keyType, Class<? extends V> valueType,
        ObjectGenerationContext context) {
        HashMap<K, V> instance = new HashMap<K, V>();
        int size = 3;
        ObjectQuery keyQuery = new ObjectQuery(keyType);
        ObjectQuery valueQuery = new ObjectQuery(valueType);
        for (int i = 0; i < size; i++) {
            context.generate(keyQuery).map(x -> (K) x).ifPresent(
                k -> context.generate(valueQuery).map(x -> (V) x)
                    .ifPresent(v -> instance.put(k, v)));
        }

        return instance;
    }

    private Class<?> getKeyType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    private Class<?> getValueType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[1];
    }

}
