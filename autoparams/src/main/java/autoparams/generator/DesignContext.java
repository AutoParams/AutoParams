package autoparams.generator;

/**
 * A specialized design context for configuring nested objects within the
 * Designer API.
 * <p>
 * This class provides the same fluent interface as {@link Designer} but is
 * specifically used for configuring nested objects through the
 * {@link DesignLanguage.ParameterBinding#using(java.util.function.Function)}
 * method. It extends {@link DesignLanguage} to inherit all configuration
 * capabilities while being tailored for nested object scenarios.
 * </p>
 *
 * <p><b>Key Characteristics:</b></p>
 * <ul>
 * <li><b>Nested Configuration:</b> Used exclusively for nested object design</li>
 * <li><b>Fluent Interface:</b> Supports the same {@code set()} and {@code process()}
 *     methods as {@link Designer}</li>
 * <li><b>Function Parameter:</b> Passed to and returned from design functions</li>
 * <li><b>Type Safety:</b> Parameterized with the nested object type</li>
 * </ul>
 *
 * <p><b>Usage in Nested Configuration:</b></p>
 * <pre>
 * Review review = Designer.design(Review.class)
 *     .set(Review::product).using(product -> product // DesignContext&lt;Product&gt;
 *         .set(Product::name).to("Laptop")
 *         .set(Product::price).to(BigDecimal.valueOf(999.99))
 *         .process(p -> p.applyDiscount(0.1))
 *     )
 *     .create();
 * </pre>
 *
 * <p><b>Design Function Contract:</b></p>
 * <p>
 * When used in {@code using()}, the design function must return the same
 * {@code DesignContext} instance it receives:
 * </p>
 * <pre>
 * // Correct usage - returns the parameter
 * .using(product -> product.set(Product::name).to("Laptop"))
 *
 * // Incorrect usage - would throw IllegalArgumentException
 * .using(product -> {
 *     product.set(Product::name).to("Laptop");
 *     return null; // Wrong! Must return the parameter
 * })
 * </pre>
 *
 * @param <T> the type of the nested object being configured
 * @see Designer
 * @see DesignLanguage
 * @see DesignLanguage.ParameterBinding#using(java.util.function.Function)
 */
public final class DesignContext<T> extends DesignLanguage<T, DesignContext<T>> {

    DesignContext() {
    }

    @Override
    DesignContext<T> context() {
        return this;
    }
}
