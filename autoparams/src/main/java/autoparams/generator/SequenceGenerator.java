package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static autoparams.generator.CollectionGenerator.getSize;

final class SequenceGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            if (isCollection((Class<?>) type.getRawType())) {
                int size = getSize(query);
                return new ObjectContainer(generateList(type, size, context));
            }
        }

        return ObjectContainer.EMPTY;
    }

    private static boolean isCollection(Class<?> type) {
        return type.equals(ArrayList.class)
            || type.equals(List.class)
            || type.equals(AbstractList.class)
            || type.equals(Collection.class)
            || type.equals(AbstractCollection.class)
            || type.equals(Iterable.class);
    }

    private static ArrayList<Object> generateList(
        ParameterizedType collectionType,
        int size,
        ResolutionContext context
    ) {
        Type elementType = collectionType.getActualTypeArguments()[0];
        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(collectionType);
        Type resolvedElementType = typeResolver.resolve(elementType);
        ObjectQuery query = new DefaultObjectQuery(resolvedElementType);
        ArrayList<Object> instance = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            instance.add(context.resolve(query));
        }

        return instance;
    }
}
