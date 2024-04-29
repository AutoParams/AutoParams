package autoparams.generator;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.TypeQuery;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Type type = query.getType();
        return isArrayType(type)
            ? generateArray((Class<?>) query.getType(), context)
            : isGenericArrayType(type)
            ? generateArray((GenericArrayType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private boolean isArrayType(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    private boolean isGenericArrayType(Type type) {
        return type instanceof GenericArrayType;
    }

    private ObjectContainer generateArray(
        Class<?> arrayType,
        ResolutionContext context
    ) {
        Class<?> elementType = arrayType.getComponentType();
        Object array = Array.newInstance(elementType, 3);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, context.resolve(new TypeQuery(elementType)));
        }
        return new ObjectContainer(array);
    }

    private ObjectContainer generateArray(
        GenericArrayType type,
        ResolutionContext context
    ) {
        return generateArray(
            (ParameterizedType) type.getGenericComponentType(),
            context
        );
    }

    private ObjectContainer generateArray(
        ParameterizedType elementType,
        ResolutionContext context
    ) {
        Class<?> rawElementType = (Class<?>) elementType.getRawType();
        ObjectQuery query = new TypeQuery(elementType);
        Object array = Array.newInstance(rawElementType, 3);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, context.resolve(query));
        }
        return new ObjectContainer(array);
    }
}
