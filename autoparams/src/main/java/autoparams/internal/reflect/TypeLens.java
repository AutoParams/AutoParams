package autoparams.internal.reflect;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides utility methods for {@link Type} objects.
 * <p>
 * This class is for internal implementation purposes and is not safe for
 * external use because its interface and behavior can change at any time.
 * </p>
 */
public final class TypeLens {

    private final Type type;

    /**
     * Constructs a {@link TypeLens} for the specified type.
     *
     * @param type the type to inspect.
     */
    public TypeLens(Type type) {
        this.type = type;
    }

    /**
     * Checks if the type inspected by this {@link TypeLens} implements the
     * specified interface type.
     *
     * @param type the interface type to check against.
     * @return {@code true} if the inspected type implements the interface,
     *         otherwise {@code false}.
     */
    public boolean implementsInterface(Type type) {
        return this.type instanceof Class
            && implementsInterface((Class<?>) this.type, type);
    }

    private static boolean implementsInterface(
        Class<?> implementationType,
        Type type
    ) {
        if (type instanceof Class) {
            return implementsInterface(implementationType, (Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return implementsInterface(
                implementationType,
                (ParameterizedType) type
            );
        } else {
            return false;
        }
    }

    private static boolean implementsInterface(
        Class<?> implementationType,
        Class<?> type
    ) {
        return type.isInterface() && type.isAssignableFrom(implementationType);
    }

    private static boolean implementsInterface(
        Class<?> implementationType,
        ParameterizedType type
    ) {
        for (AnnotatedType annotatedType :
            implementationType.getAnnotatedInterfaces()) {
            if (match(type, annotatedType.getType())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the type inspected by this {@link TypeLens} matches the
     * specified type.
     *
     * @param type the type to compare against.
     * @return {@code true} if the inspected type matches the specified type,
     *         otherwise {@code false}.
     */
    public boolean matches(Type type) {
        return match(this.type, type);
    }

    private static boolean match(Type type1, Type type2) {
        if (type1.equals(type2)) {
            return true;
        } else if (type1 instanceof ParameterizedType) {
            return match((ParameterizedType) type1, type2);
        } else {
            return false;
        }
    }

    private static boolean match(ParameterizedType type1, Type type2) {
        return type2 instanceof ParameterizedType
            && match(type1, (ParameterizedType) type2);
    }

    private static boolean match(
        ParameterizedType type1,
        ParameterizedType type2
    ) {
        if (type1.getRawType().equals(type2.getRawType()) == false) {
            return false;
        }

        return match(
            type1.getActualTypeArguments(),
            type2.getActualTypeArguments()
        );
    }

    private static boolean match(Type[] types1, Type[] types2) {
        if (types1.length != types2.length) {
            return false;
        }

        for (int i = 0; i < types1.length; i++) {
            if (match(types1[i], types2[i]) == false) {
                return false;
            }
        }

        return true;
    }
}
