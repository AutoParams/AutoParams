package autoparams.generator;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSize;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Type type = query.getType();
        if (isArrayType(type)) {
            Class<?> arrayType = (Class<?>) query.getType();
            return generateArray(
                arrayType.getComponentType(),
                getSize(query),
                context
            );
        } else if (isGenericArrayType(type)) {
            GenericArrayType arrayType = (GenericArrayType) query.getType();
            return generateArray(
                (ParameterizedType) arrayType.getGenericComponentType(),
                getSize(query),
                context
            );
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private boolean isArrayType(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    private boolean isGenericArrayType(Type type) {
        return type instanceof GenericArrayType;
    }

    private static ObjectContainer generateArray(
        Class<?> elementType,
        int size,
        ResolutionContext context
    ) {
        Object array = Array.newInstance(elementType, size);
        ObjectQuery query = new DefaultObjectQuery(elementType);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, context.resolve(query));
        }

        return new ObjectContainer(array);
    }

    private static ObjectContainer generateArray(
        ParameterizedType elementType,
        int size,
        ResolutionContext context
    ) {
        Class<?> rawElementType = (Class<?>) elementType.getRawType();
        Object array = Array.newInstance(rawElementType, size);
        ObjectQuery query = new DefaultObjectQuery(elementType);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, context.resolve(query));
        }

        return new ObjectContainer(array);
    }
}
