package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class MapGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isMap(type) ? Optional.of(factory(query, context)) : Optional.empty();
    }

    private boolean isMap(Class<?> type) {
        return type.equals(HashMap.class) || type.equals(Map.class);
    }

    private Object factory(ObjectQuery query, ObjectGenerationContext context) {
        return factory(getKeyType(query), getValueType(query), context);
    }

    @SuppressWarnings("unchecked")
    private <K, V> HashMap<K, V> factory(Class<? extends K> keyType, Class<? extends V> valueType,
            ObjectGenerationContext context) {
        HashMap<K, V> instance = new HashMap<K, V>();
        int size = 3;
        ObjectGenerator generator = context.getGenerator();
        ObjectQuery keyQuery = new ObjectQuery(keyType);
        ObjectQuery valueQuery = new ObjectQuery(valueType);
        for (int i = 0; i < size; i++) {
            generator.generate(keyQuery, context).map(x -> (K) x).ifPresent(
                    k -> generator.generate(valueQuery, context).map(x -> (V) x).ifPresent(v -> instance.put(k, v)));
        }

        return instance;
    }

    private Class<?> getKeyType(ObjectQuery query) {
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    private Class<?> getValueType(ObjectQuery query) {
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[1];
    }

}
