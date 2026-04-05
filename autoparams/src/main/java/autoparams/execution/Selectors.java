package autoparams.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static autoparams.internal.reflect.Parameters.getRequiredParameterName;

/**
 * Factory methods for creating selectors used to exclude
 * constructors, methods, and parameters from
 * {@link NullGuardValidator} validation.
 *
 * @see ConstructorSelector
 * @see MethodSelector
 * @see ParameterSelector
 */
public final class Selectors {

    private Selectors() {
    }

    /**
     * Creates a selector that matches constructors satisfying
     * the given predicate.
     *
     * @param predicate the predicate to test constructors
     * @return a new constructor selector
     */
    public static ConstructorSelector constructor(
        Predicate<Constructor<?>> predicate
    ) {
        return new ConstructorSelector(predicate);
    }

    /**
     * Creates a selector that matches constructors with the
     * given parameter types.
     *
     * @param parameterTypes the exact parameter types to match
     * @return a new constructor selector
     */
    public static ConstructorSelector constructor(
        Class<?>... parameterTypes
    ) {
        return new ConstructorSelector(c ->
            Arrays.equals(c.getParameterTypes(), parameterTypes)
        );
    }

    /**
     * Creates a selector that matches all constructors.
     *
     * @return a new constructor selector matching all
     *         constructors
     */
    public static ConstructorSelector allConstructors() {
        return new ConstructorSelector(c -> true);
    }

    /**
     * Creates a selector that matches methods satisfying the
     * given predicate.
     *
     * @param predicate the predicate to test methods
     * @return a new method selector
     */
    public static MethodSelector method(
        Predicate<Method> predicate
    ) {
        return new MethodSelector(predicate);
    }

    /**
     * Creates a selector that matches methods with the given
     * name.
     *
     * @param methodName the method name to match
     * @return a new method selector
     */
    public static MethodSelector method(String methodName) {
        return new MethodSelector(m -> m.getName().equals(methodName));
    }

    /**
     * Creates a selector that matches the method with the given
     * name and parameter types.
     *
     * @param methodName     the method name to match
     * @param parameterTypes the exact parameter types to match
     * @return a new method selector
     */
    public static MethodSelector method(
        String methodName,
        Class<?>... parameterTypes
    ) {
        return new MethodSelector(m ->
            m.getName().equals(methodName)
            && Arrays.equals(m.getParameterTypes(), parameterTypes)
        );
    }

    /**
     * Creates a selector that matches parameters satisfying the
     * given predicate.
     *
     * @param predicate the predicate to test parameters
     * @return a new parameter selector
     */
    public static ParameterSelector parameter(
        Predicate<Parameter> predicate
    ) {
        return new ParameterSelector((p, index) -> predicate.test(p));
    }

    /**
     * Creates a selector that matches parameters satisfying the
     * given predicate, which also receives the parameter index.
     *
     * @param predicate the predicate to test parameters with
     *                  their index
     * @return a new parameter selector
     */
    public static ParameterSelector parameter(
        BiPredicate<Parameter, Integer> predicate
    ) {
        return new ParameterSelector(predicate);
    }

    /**
     * Creates a selector that matches parameters with the given
     * name.
     * <p>
     * This method requires parameter names to be available at
     * runtime. If names are not available, a
     * {@link RuntimeException} is thrown at evaluation time.
     * </p>
     *
     * @param parameterName the parameter name to match
     * @return a new parameter selector
     * @throws RuntimeException if parameter names are not
     *                          available at runtime
     */
    public static ParameterSelector parameter(String parameterName) {
        return new ParameterSelector((p, index) -> {
            String name = getRequiredParameterName(p, index);
            return name.equals(parameterName);
        });
    }
}
