package autoparams.customization;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class TypeLens {

    private final Type type;

    public TypeLens(Type type) {
        this.type = type;
    }

    public boolean implementsInterface(Type type) {
        if (this.type instanceof Class) {
            return implementsInterface((Class<?>) this.type, type);
        } else {
            return false;
        }
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
        if (type.isInterface()) {
            return type.isAssignableFrom(implementationType);
        } else {
            return false;
        }
    }

    private static boolean implementsInterface(
        Class<?> implementationType,
        ParameterizedType type
    ) {
        for (AnnotatedType annotatedType :
            implementationType.getAnnotatedInterfaces()) {
            if (equals(type, annotatedType.getType())) {
                return true;
            }
        }

        return false;
    }

    public boolean matches(Type type) {
        return equals(this.type, type);
    }

    private static boolean equals(Type type1, Type type2) {
        if (type1.equals(type2)) {
            return true;
        } else if (type1 instanceof ParameterizedType) {
            return equals((ParameterizedType) type1, type2);
        } else {
            return false;
        }
    }

    private static boolean equals(ParameterizedType type1, Type type2) {
        return type2 instanceof ParameterizedType
            && equals(type1, (ParameterizedType) type2);
    }

    private static boolean equals(
        ParameterizedType type1,
        ParameterizedType type2
    ) {
        if (type1.getRawType().equals(type2.getRawType()) == false) {
            return false;
        }

        return equals(
            type1.getActualTypeArguments(),
            type2.getActualTypeArguments()
        );
    }

    private static boolean equals(Type[] types1, Type[] types2) {
        if (types1.length != types2.length) {
            return false;
        }

        for (int i = 0; i < types1.length; i++) {
            if (equals(types1[i], types2[i]) == false) {
                return false;
            }
        }

        return true;
    }
}
