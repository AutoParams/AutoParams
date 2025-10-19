package autoparams.lombok;

import autoparams.customization.Customizer;
import autoparams.customization.RecursionGuard;
import autoparams.generator.ObjectGenerator;

/**
 * Customizes object generation for types that use
 * <a href="https://projectlombok.org/">Lombok</a>'s
 * {@link lombok.Builder @Builder} annotation.
 * <p>
 * This customizer enables AutoParams to create instances of classes annotated
 * with Lombok's {@link lombok.Builder @Builder} by invoking their builder
 * pattern. By default, it assumes the static builder method is named
 * {@code builder()} and the build method is named {@code build()}.
 * </p>
 * <p>
 * To use this customizer, annotate your test method or class with
 * {@code @Customization(BuilderCustomizer.class)}.
 * </p>
 * <p>
 * This customizer automatically applies {@link RecursionGuard} to prevent
 * infinite recursion when generating objects with self-referencing fields.
 * For types with self-referencing fields (e.g., {@code Category parent} or
 * {@code Set&lt;Category&gt; children}), the recursion depth is limited to 1
 * by default, and self-referencing fields will be set to {@code null}.
 * </p>
 *
 * <p>
 * <b>
 *     Example: Object generation with Lombok's {@link lombok.Builder @Builder}
 * </b>
 * </p>
 * <p>
 * This example demonstrates how to use {@link BuilderCustomizer} to
 * automatically generate an {@code Order} object, which is built using a Lombok
 * builder:
 * </p>
 * <pre>
 * &#64;Builder
 * &#64;Getter
 * public class Order {
 *
 *     private final UUID id;
 *     private final String productName;
 *     // other fields...
 * }
 *
 * public class OrderTests {
 *
 *     &#64;Test
 *     &#64;AutoParams
 *     &#64;Customization(BuilderCustomizer.class)
 *     void testMethod(Order order) {
 *         assertNotNull(order.getId());
 *         assertNotNull(order.getProductName());
 *     }
 * }
 * </pre>
 * <p>
 * If your builder methods have custom names (e.g.,
 * {@code @Builder(builderMethodName = "newBuilder", buildMethodName = "create")}),
 * you can provide these names to the constructor of {@link BuilderCustomizer},
 * or extend {@link BuilderCustomizer} and pass the custom method names to the
 * {@code super} constructor.
 * </p>
 *
 * @see lombok.Builder
 * @see Customizer
 * @see ObjectGenerator
 * @see RecursionGuard
 * @see autoparams.customization.Customization
 */
public class BuilderCustomizer implements Customizer {

    private final BuilderInvoker invoker;

    /**
     * Creates an instance of {@link BuilderCustomizer} with default builder
     * method names ({@code "builder"}, {@code "build"}).
     */
    public BuilderCustomizer() {
        this("builder", "build");
    }

    /**
     * Creates an instance of {@link BuilderCustomizer} with specified builder
     * method names.
     * <p>
     * This constructor is used when the target class, annotated with
     * {@link lombok.Builder @Builder}, uses custom names for its static builder
     * method or the build method. For example, if a class uses
     * {@code @Builder(builderMethodName = "newBuilder", buildMethodName = "create")}),
     * you would pass {@code "newBuilder"} and {@code "create"} to this
     * constructor.
     * </p>
     *
     * @param builderMethodName The name of the static method that returns the
     *                          builder instance (e.g., {@code "newBuilder"}).
     * @param buildMethodName   The name of the method in the builder that
     *                          creates the final object instance (e.g.,
     *                          {@code "create"}).
     */
    public BuilderCustomizer(String builderMethodName, String buildMethodName) {
        this.invoker = new BuilderInvoker(builderMethodName, buildMethodName);
    }

    /**
     * Decorates the provided {@link ObjectGenerator} to enable the creation of
     * objects using Lombok's {@link lombok.Builder @Builder} pattern.
     *
     * @param generator The original {@link ObjectGenerator} to be decorated.
     * @return A new {@link ObjectGenerator} that incorporates Lombok's
     *         {@link lombok.Builder @Builder} based object generation, falling
     *         back to the original generator if the requested type does not use
     *         Lombok's {@link lombok.Builder @Builder} pattern.
     */
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return new RecursionGuard().customize(invoker.customize(generator));
    }
}
