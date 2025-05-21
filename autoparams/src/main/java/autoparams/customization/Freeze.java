package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that freezes a generated value for reuse in the following parameters.
 * <p>
 * This annotation marks a parameter as a source of a frozen value that should
 * be reused when generating other arguments of compatible types during test
 * execution. It provides a convenient way to ensure value consistency between
 * related parameters without manual setup.
 * </p>
 * <p>
 * The freezing behavior can be controlled through settings that determine which
 * targets should receive the frozen value during object generation.
 * </p>
 * <p>
 * This annotation is shorthand for {@code @FreezeBy(EXACT_TYPE)} with additional
 * configuration options.
 * </p>
 *
 * <p><b>Example: Freezing a Simple Type</b></p>
 * <p>
 * The following example demonstrates how to use {@link Freeze @Freeze} to
 * ensure that a generated string value is reused for the following string
 * parameters. This is useful for ensuring consistency across multiple arguments
 * of the same type.
 * </p>
 * <pre>
 * &#64;Test
 * &#64;AutoParams
 * void testMethod(&#64;Freeze String frozenString, String anotherString) {
 *     assertSame(frozenString, anotherString);
 * }
 * </pre>
 *
 * @see FreezeBy
 * @see RecycleArgument
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@RecycleArgument(LimitedFreezingRecycler.class)
public @interface Freeze {

    /**
     * Controls whether values should be frozen by exact type matching.
     * <p>
     * When set to {@code true}, the frozen value will be reused for all targets
     * with exactly the same type as the annotated parameter. If {@code false},
     * exact type matching will not be used.
     * </p>
     * <p>Defaults to {@code true}.</p>
     *
     * @return {@code true} if freezing by exact type is enabled, {@code false}
     *         otherwise
     */
    boolean byExactType() default true;

    /**
     * Controls whether values should be frozen by implemented interfaces.
     * <p>
     * When set to {@code true}, the frozen value will be reused for all targets
     * whose types are interfaces that the frozen value's type implements. If
     * {@code false}, interface-based matching will not be used.
     * </p>
     * <p>Defaults to {@code false}.</p>
     *
     * @return {@code true} if freezing by implemented interfaces is enabled,
     *         {@code false} otherwise
     */
    boolean byImplementedInterfaces() default false;
}
