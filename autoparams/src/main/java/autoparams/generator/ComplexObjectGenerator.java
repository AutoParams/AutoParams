package autoparams.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import autoparams.ResolutionContext;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        if (query.getType() instanceof Class<?>) {
            return generateNonGeneric((Class<?>) query.getType(), context);
        } else if (query.getType() instanceof ParameterizedType) {
            return generateGeneric((ParameterizedType) query.getType(), context);
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private ObjectContainer generateNonGeneric(Class<?> type, ResolutionContext context) {
        if (isAbstract(type)) {
            return ObjectContainer.EMPTY;
        }

        Constructor<?> constructor = context
            .generate(ConstructorResolver.class)
            .resolveOrElseThrow(type);

        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(ObjectQuery::fromParameter);

        return new ObjectContainer(createInstance(constructor, argumentQueries, context));
    }

    private ObjectContainer generateGeneric(
        ParameterizedType parameterizedType,
        ResolutionContext context
    ) {
        Class<?> type = (Class<?>) parameterizedType.getRawType();

        if (isAbstract(type) || type.equals(Builder.class)) {
            return ObjectContainer.EMPTY;
        }

        Constructor<?> constructor = context
            .generate(ConstructorResolver.class)
            .resolveOrElseThrow(type);

        Map<TypeVariable<?>, Type> genericMap = getGenericMap(type, parameterizedType);

        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(parameter -> resolveArgumentQuery(parameter, genericMap));

        return new ObjectContainer(createInstance(constructor, argumentQueries, context));
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
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
        return parameter.getParameterizedType() instanceof TypeVariable
            ? resolveParameterizedTypeQuery(parameter.getParameterizedType(), genericMap)
            : ObjectQuery.fromParameter(parameter);
    }

    private ObjectQuery resolveParameterizedTypeQuery(
        Type parameterizedType,
        Map<TypeVariable<?>, Type> genericMap
    ) {
        return ObjectQuery.fromType(genericMap.get((TypeVariable<?>) parameterizedType));
    }

    private Object createInstance(
        Constructor<?> constructor,
        Stream<ObjectQuery> argumentQueries,
        ResolutionContext context
    ) {
        try {
            return constructor.newInstance(generateArguments(argumentQueries, context));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] generateArguments(
        Stream<ObjectQuery> argumentQueries,
        ResolutionContext context
    ) {
        return argumentQueries.map(context::generate).toArray();
    }
}
