package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
        if (query instanceof GenericObjectQuery == false || isAbstractType(query.getType())) {
            return GenerationResult.absence();
        }

        return ComplexObjectConstructorResolver.resolveConstructor(query.getType())
            .map(constructor -> generate((GenericObjectQuery) query, constructor, context))
            .map(GenerationResult::presence)
            .orElse(GenerationResult.absence());
    }

    private Object generate(
        GenericObjectQuery sourceQuery,
        Constructor<?> constructor,
        ObjectGenerationContext context
    ) {
        Parameter[] parameters = constructor.getParameters();
        Stream<ObjectQuery> argumentQueries = resolveArgumentQueries(sourceQuery, parameters);
        return createInstance(constructor, argumentQueries, context);
    }

    private boolean isAbstractType(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    private Stream<ObjectQuery> resolveArgumentQueries(
        GenericObjectQuery genericObjectQuery,
        Parameter[] parameters
    ) {
        Class<?> type = genericObjectQuery.getType();
        ParameterizedType parameterizedType = genericObjectQuery.getParameterizedType();
        Map<TypeVariable<?>, Type> genericMap = getGenericMap(type, parameterizedType);
        return stream(parameters).map(parameter -> resolveArgumentQuery(parameter, genericMap));
    }

    private Map<TypeVariable<?>, Type> getGenericMap(
        Class<?> type,
        ParameterizedType parameterizedType
    ) {
        HashMap<TypeVariable<?>, Type> map = new HashMap<>();

        TypeVariable<?>[] typeVariables = type.getTypeParameters();
        Type[] typeValues = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < typeVariables.length; i++) {
            map.put(typeVariables[i], typeValues[i]);
        }

        return map;
    }

    private ObjectQuery resolveArgumentQuery(
        Parameter parameter,
        Map<TypeVariable<?>, Type> genericMap
    ) {
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

    private Object createInstance(
        Constructor<?> constructor,
        Stream<ObjectQuery> argumentQueries,
        ObjectGenerationContext context
    ) {
        try {
            return constructor.newInstance(generateArguments(argumentQueries, context));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] generateArguments(
        Stream<ObjectQuery> argumentQueries,
        ObjectGenerationContext context
    ) {
        return argumentQueries.map(context::generate).toArray();
    }

}
