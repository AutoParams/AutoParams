package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.internal.type.RuntimeTypeResolver;

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

        Parameter[] parameters = constructor.getParameters();
        ObjectQuery[] argumentQueries = new ObjectQuery[parameters.length];
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            argumentQueries[index] = new ParameterQuery(
                parameter,
                index,
                parameter.getAnnotatedType().getType()
            );
        }

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

        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(type);
        Parameter[] parameters = constructor.getParameters();
        ObjectQuery[] argumentQueries = new ObjectQuery[parameters.length];
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            argumentQueries[index] = new ParameterQuery(
                parameter,
                index,
                typeResolver.resolve(parameter.getAnnotatedType().getType())
            );
        }

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
        ObjectQuery[] argumentQueries,
        ResolutionContext context
    ) {
        try {
            Object[] arguments = Arrays
                .stream(argumentQueries)
                .map(context::resolve)
                .toArray();
            return constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
