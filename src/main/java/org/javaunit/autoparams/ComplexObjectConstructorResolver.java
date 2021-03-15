package org.javaunit.autoparams;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

final class ComplexObjectConstructorResolver {

    public static Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        return isSimpleType(type) ? Optional.empty() : Arrays.stream(type.getConstructors()).findFirst();
    }

    private static boolean isSimpleType(Class<?> type) {
        return type.equals(Boolean.class) || type.equals(Integer.class) || type.equals(Long.class)
            || type.equals(Float.class) || type.equals(Double.class) || type.equals(String.class)
            || type.equals(BigDecimal.class) || type.equals(UUID.class);
    }

}
