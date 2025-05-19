package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for applying one or more {@link Customizer} implementations to a
 * test.
 * <p>
 * This annotation can be used on another annotation, a test method, or a test
 * parameter to specify custom object generation or processing logic. The
 * {@code value} element defines an array of {@link Customizer} classes that
 * will be applied in the order specified.
 * </p>
 * <p><b>Example:</b></p>
 * <pre>
 * // Apply customizers to a test method for custom object resolution
 * &#64;Test
 * &#64;AutoParams
 * &#64;Customization({MyCustomizer1.class, MyCustomizer2.class})
 * void testMethod(MyClass arg) {
 *     // Customizers are applied to object resolution for arg
 * }
 * </pre>
 *
 * @see Customizer
 */
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Customization {

    /**
     * Specifies the {@link Customizer} classes to apply.
     * <p>
     * The customizers listed in this array are applied in the order specified.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>
     * // Apply customizers to a test method for custom object resolution
     * &#64;Test
     * &#64;AutoParams
     * &#64;Customization({MyCustomizer1.class, MyCustomizer2.class})
     * void testMethod(MyClass arg) {
     *     // Customizers are applied to object resolution for arg
     * }
     * </pre>
     *
     * @return the array of customizer classes to apply
     * @see Customizer
     */
    Class<? extends Customizer>[] value();
}
