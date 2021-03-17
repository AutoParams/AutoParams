package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

final class ComplexObjectConstructorResolver {

    interface ConstructorSelector {

        Optional<Constructor<?>> select(Constructor<?>[] constructors);

    }

    public static Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        return isSimpleType(type)
            ? Optional.empty()
            : selectConstructor(type.getConstructors());
    }

    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(
        Arrays.asList(Boolean.class, Integer.class, Long.class, Float.class, Double.class,
            String.class, BigDecimal.class, UUID.class));

    private static boolean isSimpleType(Class<?> type) {
        return SIMPLE_TYPES.stream().anyMatch(type::equals);
    }

    private static Optional<Constructor<?>> selectConstructor(Constructor<?>[] constructors) {
        ConstructorSelector[] selectors = new ConstructorSelector[] {
            source -> selectWithFewestParameters(filterDecoratedWithConstructorProperties(source)),
            source -> selectWithFewestParameters(source),
        };

        for (ConstructorSelector selector : selectors) {
            Optional<Constructor<?>> constructor = selector.select(constructors);
            if (constructor.isPresent()) {
                return constructor;
            }
        }

        return Optional.empty();
    }

    private static Optional<Constructor<?>> selectWithFewestParameters(
        Constructor<?>[] constructors) {

        return stream(constructors)
            .sorted(Comparator.comparing(c -> c.getParameterCount()))
            .findFirst();
    }

    private static Constructor<?>[] filterDecoratedWithConstructorProperties(
        Constructor<?>[] constructors) {

        return stream(constructors)
           .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
           .toArray(Constructor<?>[]::new);
    }

}
