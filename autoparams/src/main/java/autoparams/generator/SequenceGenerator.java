package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.TypeQuery;

final class SequenceGenerator implements ObjectGenerator {

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
        return isCollection((Class<?>) type.getRawType())
            ? new ObjectContainer(generateList(type, context))
            : ObjectContainer.EMPTY;
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
        ParameterizedType listType,
        ResolutionContext context
    ) {
        Type elementType = listType.getActualTypeArguments()[0];
        ObjectQuery query = new TypeQuery(elementType);
        ArrayList<Object> instance = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            instance.add(context.resolve(query));
        }
        return instance;
    }
}
