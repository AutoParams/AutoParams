package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntSupplier;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSizeSupplier;

final class SetGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            return generate(type, getSizeSupplier(query), context);
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private ObjectContainer generate(
        ParameterizedType type,
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        return isSet((Class<?>) type.getRawType())
            ? new ObjectContainer(generateSet(type, sizeSupplier, context))
            : ObjectContainer.EMPTY;
    }

    private boolean isSet(Class<?> type) {
        return type.equals(HashSet.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class);
    }

    public static HashSet<Object> generateSet(
        ParameterizedType setType,
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        Type elementType = setType.getActualTypeArguments()[0];
        ObjectQuery query = new DefaultObjectQuery(elementType);
        HashSet<Object> instance = new HashSet<>();
        int size = sizeSupplier.getAsInt();
        for (int i = 0; i < size; i++) {
            instance.add(context.resolve(query));
        }

        return instance;
    }
}
