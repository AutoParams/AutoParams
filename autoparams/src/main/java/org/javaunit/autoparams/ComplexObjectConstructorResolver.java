package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

final class ComplexObjectConstructorResolver {

    interface ConstructorSelector {

        Optional<Constructor<?>> select(Constructor<?>[] constructors);

    }

    public static Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        ConstructorSelector[] selectors = new ConstructorSelector[] {
            source -> selectWithFewestParameters(filterDecoratedWithConstructorProperties(source)),
            source -> selectWithFewestParameters(source),
        };

        for (ConstructorSelector selector : selectors) {
            Optional<Constructor<?>> constructor = selector.select(type.getConstructors());
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
