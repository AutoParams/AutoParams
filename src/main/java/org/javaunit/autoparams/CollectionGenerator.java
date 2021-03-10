package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

final class CollectionGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isCollection(type) ? Optional.of(factory(getComponentType(query), context)) : Optional.empty();
    }

    private static boolean isCollection(Class<?> type) {
        return type.equals(ArrayList.class) || type.equals(List.class) || type.equals(Collection.class)
                || type.equals(Iterable.class);
    }

    private static Class<?> getComponentType(ObjectQuery query) {
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> factory(Class<? extends T> componentType, ObjectGenerationContext context) {
        ArrayList<T> instance = new ArrayList<T>();
        int size = 3;
        ObjectGenerator generator = context.getGenerator();
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < size; i++) {
            generator.generate(query, context).map(x -> (T) x).ifPresent(instance::add);
        }

        return instance;
    }

}
