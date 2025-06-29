package autoparams.customization;

import java.util.stream.Stream;

import autoparams.generator.Factory;

/**
 * A fluent API for configuring and creating objects with specific property
 * values and processing steps.
 * <p>
 * The Designer class provides a builder pattern for object creation, allowing
 * you to set specific property values through method references, configure
 * nested objects, and apply processing steps before the final object is
 * created. This is particularly useful in test scenarios where you need
 * precise control over object state.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 * <li><b>Property Setting:</b> Use method references to set specific property values</li>
 * <li><b>Nested Object Configuration:</b> Configure complex object hierarchies
 *     with {@code withDesign}</li>
 * <li><b>Fluent Interface:</b> Chain operations for readable and maintainable code</li>
 * </ul>
 *
 * <p><b>Basic Usage:</b></p>
 * <pre>
 * Product product = Designer.design(Product.class)
 *     .set(Product::name).to("Product A")
 *     .set(Product::price).to(BigDecimal.valueOf(100))
 *     .create();
 * </pre>
 *
 * <p><b>Nested Configuration:</b></p>
 * <pre>
 * Review review = Designer.design(Review.class)
 *     .set(Review::product).withDesign(product -> product
 *         .set(Product::name).to("Product A")
 *         .set(Product::category).withDesign(category -> category
 *             .set(Category::name).to("Electronics")
 *         )
 *     )
 *     .set(Review::comment).to("Great product!")
 *     .create();
 * </pre>
 *
 * @param <T> the type of object this designer creates and configures
 * @see Designer#design(Class)
 * @see DesignLanguage#set(autoparams.customization.dsl.FunctionDelegate)
 */
public final class Designer<T> extends DesignLanguage<T, Designer<T>> {

    private final Class<T> type;

    Designer(Class<T> type) {
        this.type = type;
    }

    public static <T> Designer<T> design(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' is null.");
        }

        return new Designer<>(type);
    }

    @Override
    Designer<T> context() {
        return this;
    }

    /**
     * Creates and returns the configured object instance.
     * <p>
     * This method generates the object using the configured property values.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Product product = Designer.design(Product.class)
     *     .set(Product::name).to("Laptop")
     *     .set(Product::price).to(BigDecimal.valueOf(999.99))
     *     .create(); // Returns the configured Product
     * </pre>
     *
     * @return a new instance of type {@code T} with all configurations applied
     */
    public T create() {
        return generate();
    }

    private T generate() {
        Factory<T> factory = Factory.create(type);
        return factory.get(generators().toArray(Customizer[]::new));
    }

    /**
     * Creates an infinite stream of configured objects.
     * <p>
     * This method returns a {@code Stream<T>} that generates objects on-demand
     * using the same configuration settings as the {@code create()} method.
     * Each object in the stream is created independently with the same property
     * values applied.
     * </p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>
     * Stream&lt;Product&gt; stream = Designer.design(Product.class)
     *     .set(Product::category).to("Electronics")
     *     .set(Product::inStock).to(true)
     *     .stream();
     *
     * List&lt;Product&gt; products = stream
     *     .limit(10)
     *     .collect(Collectors.toList());
     * </pre>
     *
     * <p><b>Important Notes:</b></p>
     * <ul>
     * <li>The stream is infinite and lazy - objects are created only when consumed</li>
     * <li>Each object in the stream is independently created and configured</li>
     * <li>Remember to limit the stream to avoid infinite loops</li>
     * <li>All configured property values are applied to each object</li>
     * </ul>
     *
     * @return an infinite {@code Stream<T>} of configured objects
     * @see #create()
     */
    public Stream<T> stream() {
        return Stream.generate(this::create);
    }
}
