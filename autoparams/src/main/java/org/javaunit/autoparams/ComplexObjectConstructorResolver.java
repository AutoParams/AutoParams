package org.javaunit.autoparams;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

final class ComplexObjectConstructorResolver {

    public static Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        return isSimpleType(type)
            ? Optional.empty()
            : Arrays.stream(type.getConstructors()).findFirst();
    }

    private static Set<Class<?>> SIMPLE_TYPES = new HashSet<>(
        Arrays.asList(Boolean.class, Integer.class, Long.class, Float.class, Double.class,
            String.class, BigDecimal.class, UUID.class));

    private static boolean isSimpleType(Class<?> type) {
        return SIMPLE_TYPES.stream().anyMatch(type::equals);
    }

}
