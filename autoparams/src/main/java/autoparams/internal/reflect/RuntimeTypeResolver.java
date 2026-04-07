package autoparams.internal.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
            .collect(toMap(i -> parameters[i], i -> resolveWildcardType(arguments[i])));
    }

    private static Type resolveWildcardType(Type type) {
        if (type instanceof WildcardType) {
            return resolveWildcardTypeBounds((WildcardType) type);
        }
        return type;
    }

    private static Type resolveWildcardTypeBounds(WildcardType wildcardType) {
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length > 0) {
            return upperBounds[0];
        }
        return Object.class;
    }

    /**
     * Resolves the given type.
     *
     * @param type the type to resolve.
     * @return the resolved type.
     */
    public Type resolve(Type type) {
        return resolve(type, new HashSet<>());
    }

    private Type resolve(Type type, Set<TypeVariable<?>> visiting) {
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> variable = (TypeVariable<?>) type;
            if (typeArguments.containsKey(variable)) {
                return typeArguments.get(variable);
            }
            if (!visiting.add(variable)) {
                return null;
            }
            Type bound = variable.getBounds()[0];
            Type resolved = resolve(bound, visiting);
            visiting.remove(variable);
            return resolved != null ? resolved : toRawType(bound);
        } else if (type instanceof ParameterizedType) {
            return resolve((ParameterizedType) type, visiting);
        } else if (type instanceof WildcardType) {
            return resolveWildcardTypeBounds((WildcardType) type);
        } else {
            return type;
        }
    }

    private Type resolve(
        ParameterizedType type,
        Set<TypeVariable<?>> visiting
    ) {
        Type[] originalArgs = type.getActualTypeArguments();
        Type[] resolvedArgs = resolveTypeArguments(originalArgs, visiting);
        if (resolvedArgs == null) {
            return type.getRawType();
        }
        if (resolvedArgs == originalArgs) {
            return type;
        }
        return new ParameterizedTypeDescriptor(
            resolvedArgs,
            resolve(type.getRawType(), visiting),
            resolve(type.getOwnerType(), visiting)
        );
    }

    private Type[] resolveTypeArguments(
        Type[] originalArgs,
        Set<TypeVariable<?>> visiting
    ) {
        Type[] resolvedArgs = null;
        for (int i = 0; i < originalArgs.length; i++) {
            Type arg = resolve(originalArgs[i], visiting);
            if (arg == null) {
                return null;
            }
            if (arg != originalArgs[i] && resolvedArgs == null) {
                resolvedArgs = originalArgs.clone();
            }
            if (resolvedArgs != null) {
                resolvedArgs[i] = arg;
            }
        }
        return resolvedArgs != null ? resolvedArgs : originalArgs;
    }

    private static Type toRawType(Type type) {
        return type instanceof ParameterizedType
            ? ((ParameterizedType) type).getRawType()
            : type;
    }
}
