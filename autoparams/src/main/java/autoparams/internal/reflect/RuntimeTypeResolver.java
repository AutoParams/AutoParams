package autoparams.internal.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

/**
 * Resolves generic type variables to their runtime actual types.
 * <p>
 * This class is for internal implementation purposes and is not safe for
 * external use because its interface and behavior can change at any time.
 * </p>
 */
public final class RuntimeTypeResolver {

    private final Map<TypeVariable<?>, Type> typeArguments;

    private RuntimeTypeResolver(Map<TypeVariable<?>, Type> typeArguments) {
        this.typeArguments = typeArguments;
    }

    /**
     * Creates a {@link RuntimeTypeResolver} for the given root type.
     *
     * @param rootType the root type.
     * @return a new {@link RuntimeTypeResolver} instance.
     */
    public static RuntimeTypeResolver create(Type rootType) {
        return new RuntimeTypeResolver(getTypeArguments(rootType));
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Type rootType) {
        return rootType instanceof ParameterizedType
            ? getTypeArguments((ParameterizedType) rootType)
            : Collections.emptyMap();
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(
        ParameterizedType rootType
    ) {
        Class<?> rawType = (Class<?>) rootType.getRawType();
        TypeVariable<?>[] parameters = rawType.getTypeParameters();
        Type[] arguments = rootType.getActualTypeArguments();
        return IntStream
            .range(0, parameters.length)
            .boxed()
            .collect(toMap(i -> parameters[i], i -> arguments[i]));
    }

    /**
     * Resolves the given type.
     *
     * @param type the type to resolve.
     * @return the resolved type.
     */
    public Type resolve(Type type) {
        if (type instanceof TypeVariable<?> &&
            typeArguments.containsKey(type)) {
            return typeArguments.get(type);
        } else if (type instanceof ParameterizedType) {
            return resolve((ParameterizedType) type);
        } else {
            return type;
        }
    }

    private Type resolve(ParameterizedType parameterizedType) {
        return new ParameterizedTypeDescriptor(
            Arrays
                .stream(parameterizedType.getActualTypeArguments())
                .map(this::resolve)
                .toArray(Type[]::new),
            resolve(parameterizedType.getRawType()),
            resolve(parameterizedType.getOwnerType())
        );
    }
}
