package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return ComplexObjectConstructorResolver.resolveConstructor(query.getType())
            .map(constructor -> generate(query, constructor, context)).map(Optional::of)
            .orElse(Optional.empty());
    }

    private Object generate(ObjectQuery sourceQuery, Constructor<?> constructor,
        ObjectGenerationContext context) {
        Parameter[] parameters = constructor.getParameters();
        Stream<ObjectQuery> argumentQueries = resolveArgumentQueries(sourceQuery, parameters);
        return createInstance(constructor, argumentQueries, context);
    }

    private Stream<ObjectQuery> resolveArgumentQueries(ObjectQuery sourceQuery,
        Parameter[] parameters) {
        if (sourceQuery instanceof GenericObjectQuery) {
            return resolveArgumentQueries((GenericObjectQuery) sourceQuery, parameters);
        } else {
            return resolveArgumentQueries(parameters);
        }
    }

    private Stream<ObjectQuery> resolveArgumentQueries(GenericObjectQuery genericObjectQuery,
        Parameter[] parameters) {
        Class<?> type = genericObjectQuery.getType();
        ParameterizedType parameterizedType = genericObjectQuery.getParameterizedType();
        Map<TypeVariable<?>, Type> genericMap = getGenericMap(type, parameterizedType);
        return stream(parameters).map(parameter -> resolveArgumentQuery(parameter, genericMap));
    }

    private Stream<ObjectQuery> resolveArgumentQueries(Parameter[] parameters) {
        return stream(parameters).map(ObjectQuery::create);
    }

    private Map<TypeVariable<?>, Type> getGenericMap(Class<?> type,
        ParameterizedType parameterizedType) {
        HashMap<TypeVariable<?>, Type> map = new HashMap<TypeVariable<?>, Type>();

        TypeVariable<?>[] typeVariables = type.getTypeParameters();
        Type[] typeValues = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < typeVariables.length; i++) {
            map.put(typeVariables[i], typeValues[i]);
        }

        return map;
    }

    private ObjectQuery resolveArgumentQuery(Parameter parameter,
        Map<TypeVariable<?>, Type> genericMap) {
        if (parameter.getParameterizedType() instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) parameter.getParameterizedType();
            Type typeValue = genericMap.get(typeVariable);
            if (typeValue instanceof ParameterizedType) {
                Class<?> type = (Class<?>) typeVariable.getGenericDeclaration();
                ParameterizedType parameterizedType = (ParameterizedType) typeValue;
                return new GenericObjectQuery(type, parameterizedType);
            } else {
                return new ObjectQuery((Class<?>) typeValue);
            }
        }

        return ObjectQuery.create(parameter);
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

    private Object[] generateArguments(Stream<ObjectQuery> argumentQueries,
        ObjectGenerationContext context) {
        return argumentQueries.map(context::generate).map(a -> a.orElse(null)).toArray();
    }

}
