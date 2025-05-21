package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

/**
 * Provides a DSL for creating {@link Predicate Predicate&lt;T&gt;} of
 * {@link ParameterQuery} that can be used to filter parameters based on their
 * characteristics.
 * <p>
 * This class offers static factory methods that return predicates for common
 * parameter matching scenarios, such as name equality, suffix matching, type
 * compatibility, and more. These predicates can be used in customization
 * strategies to target specific parameters during the AutoParams resolution
 * process.
 * </p>
 *
 * @see ArgumentCustomizationDsl
 * @see FreezeArgument
 */
public class ParameterQueryDsl {

    private ParameterQueryDsl() {
    }

    /**
     * Creates a predicate that matches parameters with exact name equality.
     * <p>
     * This predicate evaluates to true when a parameter's name exactly matches
     * the specified name with case sensitivity.
     * </p>
     *
     * @param parameterName the parameter name to match (case-sensitive)
     * @return a predicate that tests for exact parameter name equality
     */
    public static Predicate<ParameterQuery> parameterNameEquals(
        String parameterName
    ) {
        return new ParameterNameEquals(parameterName);
    }

    /**
     * Creates a predicate that matches parameters with case-insensitive name
     * equality.
     * <p>
     * This predicate evaluates to true when a parameter's name matches the
     * specified name, ignoring case differences.
     * </p>
     *
     * @param parameterName the parameter name to match (case-insensitive)
     * @return a predicate that tests for case-insensitive parameter name
     *         equality
     */
    public static Predicate<ParameterQuery> parameterNameEqualsIgnoreCase(
        String parameterName
    ) {
        return new ParameterNameEqualsIgnoreCase(parameterName);
    }

    /**
     * Creates a predicate that matches parameters whose names end with a
     * specific suffix.
     * <p>
     * This predicate evaluates to true when a parameter's name ends with the
     * specified suffix, with case sensitivity.
     * </p>
     *
     * @param parameterNameSuffix the suffix to match at the end of parameter
     *                            names
     * @return a predicate that tests if parameter names end with the given
     *         suffix
     */
    public static Predicate<ParameterQuery> parameterNameEndsWith(
        String parameterNameSuffix
    ) {
        return new ParameterNameEndsWith(parameterNameSuffix);
    }

    /**
     * Creates a predicate that matches parameters whose names end with a
     * specific suffix, ignoring case.
     * <p>
     * This predicate evaluates to true when a parameter's name ends with the
     * specified suffix, regardless of case.
     * </p>
     *
     * @param parameterNameSuffix the suffix to match at the end of parameter
     *                            names (case-insensitive)
     * @return a predicate that tests if parameter names end with the given
     *         suffix, ignoring case
     */
    public static Predicate<ParameterQuery> parameterNameEndsWithIgnoreCase(
        String parameterNameSuffix
    ) {
        return new ParameterNameEndsWithIgnoreCase(parameterNameSuffix);
    }

    /**
     * Creates a predicate that matches parameters of a specific type.
     * <p>
     * This predicate evaluates to true when a parameter's type matches the
     * specified type.
     * </p>
     *
     * @param parameterType the type to match against parameter types
     * @return a predicate that tests if parameter types match the given type
     */
    public static Predicate<ParameterQuery> parameterTypeMatches(
        Type parameterType
    ) {
        return new ParameterTypeMatches(parameterType);
    }

    /**
     * Creates a predicate that matches parameters declared in a specific class.
     * <p>
     * This predicate evaluates to true when a parameter is declared in the
     * specified class.
     * </p>
     *
     * @param declaringClass the class in which parameters should be declared
     * @return a predicate that tests if parameters are declared in the given
     *         class
     */
    public static Predicate<ParameterQuery> declaringClassEquals(
        Class<?> declaringClass
    ) {
        return new DeclaringClassEquals(declaringClass);
    }
}
