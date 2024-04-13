package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Set;

import autoparams.ResolutionContext;

final class SetGenerator implements ObjectGenerator {

    private static final int SIZE = 3;

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ResolutionContext context) {
        return isSet((Class<?>) type.getRawType())
            ? new ObjectContainer(factory(type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> factory(
        Type elementType,
        ResolutionContext context
    ) {
        HashSet<T> instance = new HashSet<>();
        ObjectQuery query = new TypeQuery(elementType);
        for (int i = 0; i < SIZE; i++) {
            instance.add((T) context.resolve(query));
        }

        return instance;
    }
}
