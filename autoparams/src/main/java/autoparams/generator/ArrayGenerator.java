package autoparams.generator;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ResolutionContext;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
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

    private ObjectContainer generateArray(Class<?> arrayType, ResolutionContext context) {
        Class<?> elementType = arrayType.getComponentType();
        Object array = Array.newInstance(elementType, 3);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, generateElement(elementType, context));
        }
        return new ObjectContainer(array);
    }

    private ObjectContainer generateArray(
        GenericArrayType type,
        ResolutionContext context
    ) {
        ParameterizedType elementType = (ParameterizedType) type.getGenericComponentType();
        Object array = Array.newInstance((Class<?>) elementType.getRawType(), 3);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, generateElement(elementType, context));
        }
        return new ObjectContainer(array);
    }

    private Object generateElement(Type elementType, ResolutionContext context) {
        return context.resolve(ObjectQuery.fromType(elementType));
    }
}
