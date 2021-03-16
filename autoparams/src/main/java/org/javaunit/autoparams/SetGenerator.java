package org.javaunit.autoparams;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

final class SetGenerator extends GenericObjectGenerator {

    private static Class<?> getComponentType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> factory(Class<? extends T> componentType,
        ObjectGenerationContext context) {
        HashSet<T> instance = new HashSet<T>();
        int size = 3;
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < size; i++) {
            context.generate(query).map(x -> (T) x).ifPresent(instance::add);
        }

        return instance;
    }

    @Override
    protected Optional<Object> generate(GenericObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isSet(type) ? Optional.of(factory(getComponentType(query), context))
            : Optional.empty();
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class) || type.equals(Set.class);
    }

}
