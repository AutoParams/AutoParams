package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return resolveConstructor(query.getType()).map(constructor -> {
            Parameter[] parameters = constructor.getParameters();
            Stream<ObjectQuery> argumentQueries = query.getParameterizedType() instanceof ParameterizedType
                    ? resolveGenericArgumentQueries(parameters, getGenericTypes(query))
                    : resolveArgumentQueries(constructor.getParameters());
            return createInstance(constructor, argumentQueries, context);
        }).map(Optional::of).orElse(Optional.empty());
    }

    private Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        return isSimpleType(type) ? Optional.empty() : stream(type.getConstructors()).findFirst();
    }

    private boolean isSimpleType(Class<?> type) {
        return type.equals(Boolean.class) || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Double.class) || type.equals(String.class)
                || type.equals(BigDecimal.class) || type.equals(UUID.class);
    }

    private Stream<ObjectQuery> resolveGenericArgumentQueries(Parameter[] parameters, GenericType[] genericTypes) {
        return stream(parameters).map(parameter -> resolveGenericArgumentQuery(parameter, genericTypes));
    }

    private ObjectQuery resolveGenericArgumentQuery(Parameter parameter, GenericType[] genericTypes) {
        if (parameter.getParameterizedType() instanceof TypeVariable) {
            for (int i = 0; i < genericTypes.length; i++) {
                GenericType typeArgument = genericTypes[i];
                if (typeArgument.getTypeVariable().equals(parameter.getParameterizedType())) {
                    return resolveObjectQuery(typeArgument);
                }
            }
        }

        return ObjectQuery.create(parameter);
    }

    private ObjectQuery resolveObjectQuery(GenericType genericType) {
        Type typeValue = genericType.getTypeValue();

        if (typeValue instanceof ParameterizedType) {
            Class<?> type = (Class<?>) genericType.getTypeVariable().getGenericDeclaration();
            return new ObjectQuery(type, (ParameterizedType) typeValue);
        }

        return new ObjectQuery((Class<?>) typeValue);
    }

    private GenericType[] getGenericTypes(ObjectQuery query) {
        TypeVariable<?>[] typeVariables = query.getType().getTypeParameters();
        ParameterizedType parameterizedType = (ParameterizedType) query.getParameterizedType();
        return zip(typeVariables, parameterizedType.getActualTypeArguments());
    }

    private GenericType[] zip(TypeVariable<?>[] typeVariables, Type[] typeValues) {
        GenericType[] genericTypes = new GenericType[typeVariables.length];
        for (int i = 0; i < genericTypes.length; i++) {
            genericTypes[i] = new GenericType(typeVariables[i], typeValues[i]);
        }

        return genericTypes;
    }

    private Stream<ObjectQuery> resolveArgumentQueries(Parameter[] parameters) {
        return stream(parameters).map(ObjectQuery::create);
    }

    private Object createInstance(Constructor<?> constructor, Stream<ObjectQuery> argumentQueries,
            ObjectGenerationContext context) {
        try {
            return constructor.newInstance(generateArguments(argumentQueries, context));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] generateArguments(Stream<ObjectQuery> argumentQueries, ObjectGenerationContext context) {
        ObjectGenerator generator = context.getGenerator();
        return argumentQueries.map(query -> generator.generate(query, context)).map(a -> a.orElse(null)).toArray();
    }

}
