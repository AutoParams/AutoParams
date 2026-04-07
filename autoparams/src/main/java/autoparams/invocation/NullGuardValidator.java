package autoparams.invocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static autoparams.internal.reflect.Parameters.getParameterName;

/**
 * Validates that public constructors and methods of a given type defend
 * against null arguments for non-primitive parameters.
 * <p>
 * By default, the validator checks that an
 * {@link IllegalArgumentException} is thrown when null is passed.
 * This can be customized by providing a custom exception predicate.
 * </p>
 * <p>
 * <b>Example usage:</b>
 * </p>
 * <pre>
 * NullGuardValidator validator = new NullGuardValidator();
 * validator.validate(Order.class);
 * </pre>
 * <p>
 * <b>Example with exclusions:</b>
 * </p>
 * <pre>
 * import static autoparams.invocation.Selectors.*;
 *
 * validator.validate(Order.class, query -&gt; query
 *     .exclude(allConstructors())
 *     .exclude(method("setNote", String.class))
 *     .exclude(parameter("commentOrNull").in(method("submitReview"))));
 * </pre>
 *
 * @see Selectors
 * @see Query
 */
public class NullGuardValidator {

    private static final BiPredicate<Parameter, Exception> DEFAULT_PREDICATE =
        (parameter, exception) -> exception instanceof IllegalArgumentException;

    private final ResolutionContext context;
    private final BiPredicate<Parameter, Exception> exceptionPredicate;

    /**
     * Creates a new validator with the given
     * {@link ResolutionContext} that checks for
     * {@link IllegalArgumentException}.
     *
     * @param context the resolution context used to generate
     *                argument values and create instances
     */
    public NullGuardValidator(ResolutionContext context) {
        this(context, DEFAULT_PREDICATE);
    }

    /**
     * Creates a new validator with a default
     * {@link ResolutionContext} and a custom exception predicate.
     *
     * @param exceptionPredicate a predicate that determines whether
     *                           a thrown exception is acceptable
     */
    public NullGuardValidator(Predicate<Exception> exceptionPredicate) {
        this(new ResolutionContext(), (parameter, exception) -> exceptionPredicate.test(exception));
    }

    /**
     * Creates a new validator with the given
     * {@link ResolutionContext} and a custom exception predicate.
     *
     * @param context            the resolution context used to generate
     *                           argument values and create instances
     * @param exceptionPredicate a predicate that determines whether
     *                           a thrown exception is acceptable
     */
    public NullGuardValidator(
        ResolutionContext context,
        Predicate<Exception> exceptionPredicate
    ) {
        this(context, (parameter, exception) -> exceptionPredicate.test(exception));
    }

    /**
     * Creates a new validator with a default
     * {@link ResolutionContext} and a custom exception predicate
     * that also receives the parameter being tested.
     *
     * @param exceptionPredicate a predicate that determines whether
     *                           a thrown exception is acceptable,
     *                           given the parameter and the exception
     */
    public NullGuardValidator(BiPredicate<Parameter, Exception> exceptionPredicate) {
        this(new ResolutionContext(), exceptionPredicate);
    }

    /**
     * Creates a new validator with the given
     * {@link ResolutionContext} and a custom exception predicate
     * that also receives the parameter being tested.
     *
     * @param context            the resolution context used to generate
     *                           argument values and create instances
     * @param exceptionPredicate a predicate that determines whether
     *                           a thrown exception is acceptable,
     *                           given the parameter and the exception
     */
    public NullGuardValidator(
        ResolutionContext context,
        BiPredicate<Parameter, Exception> exceptionPredicate
    ) {
        this.context = context;
        this.exceptionPredicate = exceptionPredicate;
    }

    /**
     * Validates null defense for the given type, applying
     * exclusions defined by the query function.
     *
     * @param type          the type to validate
     * @param queryFunction a function that configures exclusions
     *                      on a {@link Query}
     * @throws AssertionError if any violations are found
     */
    public void validate(
        Class<?> type,
        Function<Query, Query> queryFunction
    ) {
        Query query = queryFunction.apply(new Query());
        List<String> violations = new ArrayList<>();
        validateConstructors(type, query, violations);
        validateMethods(type, query, violations);
        if (!violations.isEmpty()) {
            throw new AssertionError(
                formatMessage(type, violations)
            );
        }
    }

    /**
     * Validates null defense for all public constructors and
     * methods of the given type.
     *
     * @param type the type to validate
     * @throws AssertionError if any violations are found
     */
    public void validate(Class<?> type) {
        validate(type, q -> q);
    }

    private void validateConstructors(
        Class<?> type,
        Query query,
        List<String> violations
    ) {
        for (Constructor<?> constructor : query.selectConstructors(type)) {
            validateConstructor(
                type, constructor, query::selectsParameter, violations
            );
        }
    }

    private void validateConstructor(
        Class<?> type,
        Constructor<?> constructor,
        BiPredicate<Parameter, Integer> parameterPredicate,
        List<String> violations
    ) {
        validateParameters(
            constructor.getParameters(),
            formatConstructorSignature(type, constructor.getParameters()),
            parameterPredicate,
            constructor::newInstance,
            violations
        );
    }

    private void validateMethods(
        Class<?> type,
        Query query,
        List<String> violations
    ) {
        for (Method method : query.selectMethods(type)) {
            validateParameters(
                method.getParameters(),
                formatMethodSignature(method, method.getParameters()),
                query::selectsParameter,
                args -> method.invoke(context.resolve(type), args),
                violations
            );
        }
    }

    private void validateParameters(
        Parameter[] params,
        String signature,
        BiPredicate<Parameter, Integer> parameterPredicate,
        Invoker invoker,
        List<String> violations
    ) {
        Object[] source = new Object[params.length];
        boolean[] resolved = new boolean[params.length];
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                args[i - 1] = getArgument(params, source, resolved, i - 1);
            }
            if (params[i].getType().isPrimitive()) {
                continue;
            }
            if (!parameterPredicate.test(params[i], i)) {
                continue;
            }
            for (int j = i + 1; j < params.length; j++) {
                args[j] = getArgument(params, source, resolved, j);
            }
            args[i] = null;
            try {
                invoker.invoke(args);
            } catch (InvocationTargetException e) {
                if (exceptionPredicate.test(params[i], (Exception) e.getCause())) {
                    continue;
                }
                violations.add(formatViolation(
                    signature, params[i], i,
                    (Exception) e.getCause()
                ));
                continue;
            } catch (Exception e) {
                continue;
            }
            violations.add(formatViolation(
                signature, params[i], i, null
            ));
        }
    }

    private Object getArgument(
        Parameter[] params,
        Object[] source,
        boolean[] resolved,
        int index
    ) {
        if (!resolved[index]) {
            Parameter param = params[index];
            RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(
                param.getDeclaringExecutable().getDeclaringClass()
            );
            ParameterQuery query = new ParameterQuery(
                param,
                index,
                typeResolver.resolve(param.getParameterizedType())
            );
            source[index] = context.resolve(query);
            resolved[index] = true;
        }
        return source[index];
    }

    @FunctionalInterface
    private interface Invoker {

        void invoke(Object[] args) throws Exception;
    }

    private static String formatConstructorSignature(
        Class<?> type,
        Parameter[] params
    ) {
        return type.getSimpleName() + "(" + formatParameterTypes(params) + ")";
    }

    private static String formatMethodSignature(
        Method method,
        Parameter[] params
    ) {
        return method.getDeclaringClass().getSimpleName()
            + "." + method.getName()
            + "(" + formatParameterTypes(params) + ")";
    }

    private static String formatParameterTypes(Parameter[] params) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Parameter param : params) {
            joiner.add(param.getType().getSimpleName());
        }
        return joiner.toString();
    }

    private static String formatViolation(
        String signature,
        Parameter parameter,
        int index,
        Exception exception
    ) {
        String detail;
        if (exception == null) {
            detail = "accepted null without throwing.";
        } else if (exception.getMessage() != null) {
            detail = "threw " + exception.getClass().getSimpleName()
                + " with message \"" + exception.getMessage()
                + "\", which does not satisfy the condition.";
        } else {
            detail = "threw " + exception.getClass().getSimpleName()
                + ", which does not satisfy the condition.";
        }
        String parameterDescription = getParameterName(parameter, index)
            .map(name -> "parameter '" + name + "' at index " + index)
            .orElse("parameter at index " + index);
        return "  " + signature + ": " + parameterDescription
            + " " + detail;
    }

    private static final String LINE_SEPARATOR =
        System.lineSeparator();

    private static String formatMessage(
        Class<?> type,
        List<String> violations
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("NullGuardValidator found ")
            .append(violations.size())
            .append(" violation(s) in ")
            .append(type.getSimpleName())
            .append(". Expected IllegalArgumentException or an")
            .append(" exception satisfying the configured")
            .append(" condition for null arguments.")
            .append(LINE_SEPARATOR);
        for (String violation : violations) {
            sb.append(LINE_SEPARATOR)
                .append(violation)
                .append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    /**
     * An immutable query that configures which constructors,
     * methods, and parameters to exclude from validation.
     *
     * @see NullGuardValidator#validate(Class, Function)
     */
    public static class Query {

        private final Predicate<Constructor<?>> constructorPredicate;
        private final Predicate<Method> methodPredicate;
        private final BiPredicate<Parameter, Integer> parameterPredicate;

        Query() {
            this(c -> true, m -> true, (p, index) -> true);
        }

        private Query(
            Predicate<Constructor<?>> constructorPredicate,
            Predicate<Method> methodPredicate,
            BiPredicate<Parameter, Integer> parameterPredicate
        ) {
            this.constructorPredicate = constructorPredicate;
            this.methodPredicate = methodPredicate;
            this.parameterPredicate = parameterPredicate;
        }

        /**
         * Returns a new query that also excludes constructors
         * matching the given selector.
         *
         * @param selector the constructor selector
         * @return a new query with the exclusion applied
         */
        public Query exclude(ConstructorSelector selector) {
            return new Query(
                constructorPredicate.and(c -> !selector.matches(c)),
                methodPredicate,
                parameterPredicate
            );
        }

        /**
         * Returns a new query that also excludes methods
         * matching the given selector.
         *
         * @param selector the method selector
         * @return a new query with the exclusion applied
         */
        public Query exclude(MethodSelector selector) {
            return new Query(
                constructorPredicate,
                methodPredicate.and(m -> !selector.matches(m)),
                parameterPredicate
            );
        }

        /**
         * Returns a new query that also excludes parameters
         * matching the given selector.
         *
         * @param selector the parameter selector
         * @return a new query with the exclusion applied
         */
        public Query exclude(ParameterSelector selector) {
            return new Query(
                constructorPredicate,
                methodPredicate,
                parameterPredicate.and(
                    (p, index) -> !selector.matches(p, index)
                )
            );
        }

        boolean selectsParameter(Parameter parameter, int index) {
            return parameterPredicate.test(parameter, index);
        }

        Collection<Method> selectMethods(Class<?> type) {
            List<Method> result = new ArrayList<>();
            for (Method method : type.getMethods()) {
                if (method.getDeclaringClass().equals(Object.class)) {
                    continue;
                }
                if (methodPredicate.test(method)) {
                    result.add(method);
                }
            }
            return result;
        }

        Collection<Constructor<?>> selectConstructors(Class<?> type) {
            List<Constructor<?>> result = new ArrayList<>();
            for (Constructor<?> constructor : type.getConstructors()) {
                if (constructorPredicate.test(constructor)) {
                    result.add(constructor);
                }
            }
            return result;
        }
    }
}
