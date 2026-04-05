package autoparams.execution;

import java.lang.reflect.Constructor;
import java.util.function.Predicate;

/**
 * Holds a rule for selecting constructors.
 *
 * @see Selectors#constructor(java.util.function.Predicate)
 * @see Selectors#constructor(Class[])
 * @see Selectors#allConstructors()
 */
public class ConstructorSelector {

    private final Predicate<Constructor<?>> predicate;

    ConstructorSelector(Predicate<Constructor<?>> predicate) {
        this.predicate = predicate;
    }

    boolean matches(Constructor<?> constructor) {
        return predicate.test(constructor);
    }
}
