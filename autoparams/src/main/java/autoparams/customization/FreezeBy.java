package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for freezing arguments by matching criteria in parameterized
 * tests.
 * <p>
 * Use this annotation on a test parameter to freeze the argument that matches
 * the specified {@link Matching} strategies. This allows the same instance to
 * be injected into other parameters that match the criteria, enabling
 * consistent object reuse within a test method.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <p>
 * Reuse a frozen value for other targets with matching names. This example
 * demonstrates how {@link Matching#PARAMETER_NAME} can be used to freeze a
 * value and inject it into other parameters with the same name. In this case,
 * the {@code reviewerId} test method argument is frozen and injected into the
 * {@code reviewerId} constructor parameter of the {@code Review} class,
 * ensuring that {@code review.getReviewerId()} returns the same instance as the
 * test method parameter.
 * </p>
 * <pre>
 * &#64;AllArgsConstructor
 * &#64;Getter
 * public class Review {
 *     private final UUID id;
 *     private final UUID reviewerId;
 *     private final Product product;
 *     private final int rating;
 *     private final String comment;
 * }
 * </pre>
 *
 * <pre>
 * import static autoparams.customization.Matching.PARAMETER_NAME;
 *
 * public class TestClass {
 *
 *     &#64;Test
 *     &#64;AutoParams
 *     void testMethod(&#64;FreezeBy(PARAMETER_NAME) UUID reviewerId, Review review) {
 *         assertNotSame(reviewerId, review.getId());
 *         assertSame(reviewerId, review.getReviewerId());
 *     }
 * }
 * </pre>
 *
 * @see Matching
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@RecycleArgument(FreezingRecycler.class)
public @interface FreezeBy {

    /**
     * Specifies the matching strategies that determine which parameters or fields
     * will receive the frozen value.
     * <p>
     * Provide one or more {@link Matching} strategies to control how the frozen
     * value is injected into other parameters or fields. Each strategy defines
     * a different matching rule, such as exact type, implemented interfaces, or
     * parameter/field name.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * This example shows how to freeze a value for all targets with the exact
     * same type.
     * </p>
     * <pre>
     * import static autoparams.customization.Matching.EXACT_TYPE;
     *
     * public class TestClass {
     *
     *     &#64;Test
     *     &#64;AutoParams
     *     void testMethod(&#64;FreezeBy(EXACT_TYPE) String s1, String s2) {
     *         assertSame(s1, s2);
     *     }
     * }
     * </pre>
     *
     * @return the array of matching strategies to apply
     * @see Matching
     */
    Matching[] value();
}
