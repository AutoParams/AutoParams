package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

import autoparams.ResolutionContext;
import autoparams.generic.RuntimeTypeResolver;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return generate(context, query.getType());
    }

    private ObjectContainer generate(ResolutionContext context, Type type) {
        if (type instanceof Class<?>) {
            return generateObject((Class<?>) type, context);
        } else if (type instanceof ParameterizedType) {
            return generateObject((ParameterizedType) type, context);
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private ObjectContainer generateObject(
        Class<?> type,
        ResolutionContext context
    ) {
        if (isAbstract(type)) {
            return ObjectContainer.EMPTY;
        }

        Constructor<?> constructor = resolveConstructor(type, context);

        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(ObjectQuery::fromParameter);

        Object value = createInstance(constructor, argumentQueries, context);
        return new ObjectContainer(value);
    }

    private ObjectContainer generateObject(
        ParameterizedType type,
        ResolutionContext context
    ) {
        Class<?> rawType = (Class<?>) type.getRawType();

        if (isAbstract(rawType)) {
            return ObjectContainer.EMPTY;
        }

        Constructor<?> constructor = resolveConstructor(rawType, context);

        RuntimeTypeResolver runtimeTypeResolver = RuntimeTypeResolver.of(type);
        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(Parameter::getParameterizedType)
            .map(runtimeTypeResolver::resolve)
            .map(TypeQuery::new);

        Object value = createInstance(constructor, argumentQueries, context);
        return new ObjectContainer(value);
    }

    private static Constructor<?> resolveConstructor(
        Class<?> type,
        ResolutionContext context
    ) {
        return context
            .resolve(ConstructorResolver.class)
            .resolveOrElseThrow(type);
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    private Object createInstance(
        Constructor<?> constructor,
        Stream<ObjectQuery> argumentQueries,
        ResolutionContext context
    ) {
        try {
            Object[] args = argumentQueries.map(context::resolve).toArray();
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
