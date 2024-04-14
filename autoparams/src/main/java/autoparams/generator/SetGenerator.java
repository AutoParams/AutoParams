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
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(
        ParameterizedType type,
        ResolutionContext context
    ) {
        return isSet((Class<?>) type.getRawType())
            ? new ObjectContainer(generateSet(type, context))
            : ObjectContainer.EMPTY;
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class);
    }

    public static HashSet<Object> generateSet(
        ParameterizedType setType,
        ResolutionContext context
    ) {
        Type elementType = setType.getActualTypeArguments()[0];
        ObjectQuery query = new TypeQuery(elementType);
        HashSet<Object> instance = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            instance.add(context.resolve(query));
        }
        return instance;
    }
}
