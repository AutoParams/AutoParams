package org.javaunit.autoparams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

final class CollectionGenerator extends GenericObjectGenerator {

    @Override
    protected Optional<Object> generate(GenericObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return isCollection(type) ? Optional.of(factory(getComponentType(query), context)) : Optional.empty();
    }

    private static boolean isCollection(Class<?> type) {
        return type.equals(ArrayList.class) || type.equals(List.class) || type.equals(Collection.class)
                || type.equals(Iterable.class);
    }

    private static Class<?> getComponentType(GenericObjectQuery query) {
        return (Class<?>) query.getParameterizedType().getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> factory(Class<? extends T> componentType, ObjectGenerationContext context) {
        ArrayList<T> instance = new ArrayList<>();
        int size = 3;
        ObjectGenerator generator = context.getGenerator();
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < size; i++) {
            generator.generate(query, context).map(x -> (T) x).ifPresent(instance::add);
        }

        return instance;
    }

}
