package autoparams.generator;

import autoparams.Builder;
import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.full.KClasses;
import kotlin.reflect.jvm.ReflectJvmMapping;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        if (query.getType() instanceof Class<?>) {
            return generateNonGeneric((Class<?>) query.getType(), context);
        } else if (query.getType() instanceof ParameterizedType) {
            return generateGeneric((ParameterizedType) query.getType(), context);
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private ObjectContainer generateNonGeneric(Class<?> type, ObjectGenerationContext context) {
        if (isAbstract(type)) {
            return ObjectContainer.EMPTY;
        }

        Constructor<?> constructor = resolveConstructor(type);
        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(ObjectQuery::fromParameter);
        return new ObjectContainer(createInstance(constructor, argumentQueries, context));
    }

    private ObjectContainer generateGeneric(
        ParameterizedType parameterizedType,
        ObjectGenerationContext context
    ) {
        Class<?> type = (Class<?>) parameterizedType.getRawType();

        if (isAbstract(type) || type.equals(Builder.class)) {
            return ObjectContainer.EMPTY;
        }

        Map<TypeVariable<?>, Type> genericMap = getGenericMap(type, parameterizedType);
        Constructor<?> constructor = resolveConstructor(type);
        Stream<ObjectQuery> argumentQueries = Arrays
            .stream(constructor.getParameters())
            .map(parameter -> resolveArgumentQuery(parameter, genericMap));
        return new ObjectContainer(createInstance(constructor, argumentQueries, context));
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    private Constructor<?> resolveConstructor(Class<?> type) {
        return ConstructorResolver
            .compose(
                t -> Optional.ofNullable(tryGetKotlinPrimaryConstructor(t)),
                t -> Arrays
                    .stream(t.getConstructors())
                    .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                    .min(Comparator.comparing(Constructor::getParameterCount)),
                t -> Arrays
                    .stream(t.getConstructors())
                    .min(Comparator.comparing(Constructor::getParameterCount))
            )
            .resolve(type)
            .orElseThrow(() -> new RuntimeException(
                "Class '" + type.getName() + "' has no public constructor."));
    }

    private <T> Constructor<T> tryGetKotlinPrimaryConstructor(Class<T> clazz) {
        try {
            final KClass<T> kotlinClass = JvmClassMappingKt.getKotlinClass(clazz);
            final KFunction<T> primaryConstructor =
                KClasses.getPrimaryConstructor(kotlinClass);
            if (primaryConstructor == null) {
                return null;
            }

            return ReflectJvmMapping.getJavaConstructor(primaryConstructor);
        } catch (Throwable e) {
            return null;
        }

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
        ObjectGenerationContext context
    ) {
        try {
            return constructor.newInstance(generateArguments(argumentQueries, context));
        } catch (Exception e) {
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
