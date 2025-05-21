package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.ParameterizedTest;

/**
 * Annotation that enables multiple executions of parameterized tests that are
 * supplied with arguments from providers like {@link AutoSource @AutoSource} or
 * {@link ValueAutoSource @ValueAutoSource}.
 * <p>
 * When applied to a test method, {@link Repeat @Repeat} causes the test to be
 * executed a specified number of times. It relies on an argument provider (like
 * {@link AutoSource @AutoSource} or {@link ValueAutoSource @ValueAutoSource})
 * to supply arguments for each execution. This combination allows each
 * repetition to receive fresh, automatically generated arguments or to iterate
 * through a set of predefined values multiple times.
 * </p>
 * <p>
 * The number of repetitions is controlled by the {@link #value} element.
 * </p>
 *
 * <p><b>Example: Repeating a test with auto-generated values</b></p>
 * <p>
 * The following test method will be executed 10 times, each time with new
 * automatically generated values for {@code a} and {@code b}.
 * </p>
 * <pre>
 * &#64;org.junit.jupiter.params.ParameterizedTest
 * &#64;autoparams.AutoSource
 * &#64;autoparams.Repeat(10)
 * void testMethod(int a, int b) {
 *     // Test logic using a and b
 * }
 * </pre>
 *
 * <p>
 * <b>Example: Repeating a test with explicit and auto-generated values</b>
 * </p>
 * <p>
 * This test method will be executed 15 times in total (5 repetitions for each
 * of the 3 values provided for {@code a}). For each run, {@code b} will be
 * auto-generated.
 * </p>
 * <pre>
 * &#64;org.junit.jupiter.params.ParameterizedTest
 * &#64;autoparams.ValueAutoSource(ints = { 1, 2, 3 })
 * &#64;autoparams.Repeat(5)
 * void testMethod(int a, int b) {
 *     // Test logic using a and b
 * }
 * </pre>
 *
 * @see #value
 * @see AutoSource
 * @see ValueAutoSource
 * @see ParameterizedTest
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Repeat {

    /**
     * Specifies the number of times the parameterized test should be repeated.
     * <p>
     * The test method, in conjunction with its argument providers (like
     * {@link AutoSource @AutoSource} or
     * {@link ValueAutoSource @ValueAutoSource}), will be executed this many
     * times.
     * </p>
     * <p>
     * Defaults to {@code 3}.
     * </p>
     *
     * @return the number of repetitions.
     */
    int value() default 3;
}
