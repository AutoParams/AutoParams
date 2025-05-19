package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies which implementation classes to use for interface types.
 * <p>
 * This annotation allows you to provide concrete implementations for interface
 * parameters in your tests. When a parameter's type is an interface, AutoParams
 * will use the specified implementation class to create an instance.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <p>
 * This example demonstrates how to use the {@link UseImplementation} annotation
 * to provide a concrete implementation for an interface.
 * </p>
 * <pre>
 * public static class HelloWorldFactory implements Supplier&lt;String&gt; {
 *     &#64;Override
 *     public String get() {
 *         return "hello world";
 *     }
 * }
 *
 * &#64;Test
 * &#64;AutoParams
 * &#64;UseImplementation(HelloWorldFactory.class)
 * void testMethod(Supplier&lt;String&gt; supplier) {
 *     assertThat(supplier.get()).isEqualTo("hello world");
 * }
 * </pre>
 */
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(ImplementationGeneratorCompositeFactory.class)
public @interface UseImplementation {

    /**
     * Specifies the implementation classes to use for interfaces.
     * <p>
     * Each implementation class must implement at least one interface. When
     * AutoParams encounters a parameter of an interface type, it will check if
     * any of the specified implementation classes can be used to create an
     * instance for that parameter.
     * </p>
     *
     * @return an array of implementation classes
     */
    Class<?>[] value();
}
