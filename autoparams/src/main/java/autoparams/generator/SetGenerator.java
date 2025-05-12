package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Set;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSize;

final class SetGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            if (isSet((Class<?>) type.getRawType())) {
                int size = getSize(query);
                return new ObjectContainer(generateSet(type, size, context));
            }
        }

        return ObjectContainer.EMPTY;
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class);
    }

    public static HashSet<Object> generateSet(
        ParameterizedType setType,
        int size,
        ResolutionContext context
    ) {
        Type elementType = setType.getActualTypeArguments()[0];
        ObjectQuery query = new DefaultObjectQuery(elementType);
        HashSet<Object> instance = new HashSet<>();
        for (int i = 0; i < size; i++) {
            instance.add(context.resolve(query));
        }

        return instance;
    }
}
