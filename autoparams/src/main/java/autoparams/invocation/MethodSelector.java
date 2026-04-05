package autoparams.invocation;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Holds a rule for selecting methods.
 *
 * @see Selectors#method(java.util.function.Predicate)
 * @see Selectors#method(String)
 * @see Selectors#method(String, Class[])
 */
public class MethodSelector {

    private final Predicate<Method> predicate;

    MethodSelector(Predicate<Method> predicate) {
        this.predicate = predicate;
    }

    boolean matches(Method method) {
        return predicate.test(method);
    }
}
