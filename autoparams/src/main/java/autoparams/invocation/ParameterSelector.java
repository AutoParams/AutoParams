package autoparams.invocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.BiPredicate;

/**
 * Holds a rule for selecting parameters.
 * <p>
 * Use the {@link #in(ConstructorSelector)} or
 * {@link #in(MethodSelector)} methods to scope the selector
 * to a specific constructor or method.
 * </p>
 *
 * @see Selectors#parameter(java.util.function.Predicate)
 * @see Selectors#parameter(java.util.function.BiPredicate)
 * @see Selectors#parameter(String)
 */
public class ParameterSelector {

    private final BiPredicate<Parameter, Integer> predicate;

    ParameterSelector(BiPredicate<Parameter, Integer> predicate) {
        this.predicate = predicate;
    }

    /**
     * Returns a new selector that matches only parameters
     * belonging to constructors matching the given scope.
     *
     * @param scope the constructor selector to scope to
     * @return a new scoped parameter selector
     */
    public ParameterSelector in(ConstructorSelector scope) {
        return new ParameterSelector((p, index) -> {
            if (!(p.getDeclaringExecutable() instanceof Constructor)) {
                return false;
            }
            Constructor<?> ctor =
                (Constructor<?>) p.getDeclaringExecutable();
            return scope.matches(ctor) && predicate.test(p, index);
        });
    }

    /**
     * Returns a new selector that matches only parameters
     * belonging to methods matching the given scope.
     *
     * @param scope the method selector to scope to
     * @return a new scoped parameter selector
     */
    public ParameterSelector in(MethodSelector scope) {
        return new ParameterSelector((p, index) -> {
            if (!(p.getDeclaringExecutable() instanceof Method)) {
                return false;
            }
            Method method = (Method) p.getDeclaringExecutable();
            return scope.matches(method) && predicate.test(p, index);
        });
    }

    boolean matches(Parameter parameter, int index) {
        return predicate.test(parameter, index);
    }
}
