package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntSupplier;
import javax.validation.constraints.Size;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class SequenceGenerator implements ObjectGenerator {

    private static final int DEFAULT_SIZE = 3;

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            return generate(
                (ParameterizedType) query.getType(),
                getSizeSupplier(query),
                context
            );
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private static ObjectContainer generate(
        ParameterizedType type,
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        return isCollection((Class<?>) type.getRawType())
            ? new ObjectContainer(generateList(type, sizeSupplier, context))
            : ObjectContainer.EMPTY;
    }

    private static IntSupplier getSizeSupplier(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getSizeSupplier((ParameterQuery) query)
            : SequenceGenerator::getDefaultSize;
    }

    private static IntSupplier getSizeSupplier(ParameterQuery query) {
        Size size = query.getParameter().getAnnotation(Size.class);
        return size == null ? SequenceGenerator::getDefaultSize : size::min;
    }

    private static int getDefaultSize() {
        return DEFAULT_SIZE;
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
        IntSupplier sizeSupplier,
        ResolutionContext context
    ) {
        Type elementType = collectionType.getActualTypeArguments()[0];
        ObjectQuery query = new DefaultObjectQuery(elementType);
        ArrayList<Object> instance = new ArrayList<>();
        int size = sizeSupplier.getAsInt();
        for (int i = 0; i < size; i++) {
            instance.add(context.resolve(query));
        }

        return instance;
    }
}
