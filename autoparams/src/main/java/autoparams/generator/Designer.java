package autoparams.generator;

import java.util.function.Consumer;

import autoparams.customization.Customizer;

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
 * <li><b>Object Processing:</b> Apply post-creation processing steps</li>
 * <li><b>Fluent Interface:</b> Chain operations for readable and maintainable code</li>
 * </ul>
 *
 * <p><b>Basic Usage:</b></p>
 * <pre>
 * Product product = Factory
 *     .design(Product.class)
 *     .set(Product::name).to("Product A")
 *     .set(Product::price).to(BigDecimal.valueOf(100))
 *     .create();
 * </pre>
 *
 * <p><b>Nested Configuration:</b></p>
 * <pre>
 * Review review = Factory
 *     .design(Review.class)
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
 * <p><b>With Processing:</b></p>
 * <pre>
 * Order order = Factory
 *     .design(Order.class)
 *     .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
 *     .process(o -> o.applyDiscount(BigDecimal.valueOf(10)))
 *     .process(o -> o.calculateTax())
 *     .create();
 * </pre>
 *
 * @param <T> the type of object this designer creates and configures
 * @see Factory#design(Class)
 * @see DesignLanguage#set(autoparams.customization.dsl.FunctionDelegate)
 * @see DesignLanguage#process(java.util.function.Consumer)
 */
public class Designer<T> extends DesignLanguage<T, Designer<T>> {

    private Factory<T> factory;

    Designer(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    Designer<T> context() {
        return this;
    }

    /**
     * Creates and returns the configured object instance.
     * <p>
     * This method generates the object using the configured property values
     * and then applies any processing steps that were specified. The creation
     * process follows these steps:
     * </p>
     * <ol>
     * <li>Generate the object with all configured property values</li>
     * <li>Apply all processing steps in the order they were added</li>
     * <li>Return the final processed object</li>
     * </ol>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Product product = Factory
     *     .design(Product.class)
     *     .set(Product::name).to("Laptop")
     *     .set(Product::price).to(BigDecimal.valueOf(999.99))
     *     .process(p -> p.applyDiscount(0.1))
     *     .create(); // Returns the configured and processed Product
     * </pre>
     *
     * @return a new instance of type {@code T} with all configurations
     *         and processing steps applied
     */
    public T create() {
        T object = generate();        
        process(object);        
        return object;
    }

    private T generate() {
        return factory.get(generators().toArray(Customizer[]::new));
    }

    private void process(T object) {
        for (Consumer<T> processor : processors()) {
            processor.accept(object);
        }
    }
}
