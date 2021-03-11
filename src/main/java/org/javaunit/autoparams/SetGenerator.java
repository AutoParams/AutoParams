package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

final class SetGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isSet(type) ? Optional.of(factory(getComponentType(query), context)) : Optional.empty();
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class) || type.equals(Set.class);
    }

    private static Class<?> getComponentType(ObjectQuery query) {
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> factory(Class<? extends T> componentType, ObjectGenerationContext context) {
        HashSet<T> instance = new HashSet<T>();
        int size = 3;
        ObjectGenerator generator = context.getGenerator();
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < size; i++) {
            generator.generate(query, context).map(x -> (T) x).ifPresent(instance::add);
        }

        return instance;
    }

}
