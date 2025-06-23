package autoparams.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.customization.dsl.ArgumentCustomizationDsl;
import autoparams.customization.dsl.FunctionDelegate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Abstract base class providing a fluent interface for object configuration
 * in the Designer API.
 * <p>
 * This class defines the core fluent design language that enables intuitive
 * and readable object configuration through method chaining. It provides the
 * foundational methods {@code set()} and {@code process()} that are used by
 * both {@link Designer} for main object configuration and {@link DesignContext}
 * for nested object configuration.
 * </p>
 *
 * <p><b>Core Design Patterns:</b></p>
 * <ul>
 * <li><b>Fluent Interface:</b> All methods return a context to enable method chaining</li>
 * <li><b>Builder Pattern:</b> Accumulates configuration before final object creation</li>
 * <li><b>Type Safety:</b> Compile-time checking of property types and method references</li>
 * <li><b>Generic Design:</b> Works with any object type through parameterization</li>
 * </ul>
 *
 * <p><b>Property Configuration:</b></p>
 * <p>
 * The {@link #set(FunctionDelegate)} method accepts method references to identify
 * properties and returns a {@link ParameterBinding} that provides two configuration
 * options:
 * </p>
 * <ul>
 * <li><b>{@link ParameterBinding#to(Object)}:</b> Set a simple value</li>
 * <li><b>{@link ParameterBinding#withDesign(Function)}:</b> Configure nested objects</li>
 * </ul>
 *
 * <p><b>Object Processing:</b></p>
 * <p>
 * The {@link #process(Consumer)} method allows custom post-creation processing
 * through lambda expressions or method references. Multiple processors can be
 * chained and are executed in the order they are added.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>
 * // Simple property setting
 * Factory.design(Product.class)
 *     .set(Product::name).to("Laptop")
 *     .set(Product::price).to(BigDecimal.valueOf(999.99))
 *     .create();
 *
 * // Nested object configuration
 * Factory.design(Review.class)
 *     .set(Review::product).withDesign(product -> product
 *         .set(Product::name).to("Laptop")
 *         .set(Product::category).withDesign(category -> category
 *             .set(Category::name).to("Electronics")
 *         )
 *     )
 *     .create();
 *
 * // Object processing
 * Factory.design(Order.class)
 *     .set(Order::originalPrice).to(BigDecimal.valueOf(100))
 *     .process(order -> order.applyDiscount(0.1))
 *     .process(order -> order.calculateTax())
 *     .create();
 * </pre>
 *
 * <p><b>Implementation Classes:</b></p>
 * <ul>
 * <li><b>{@link Designer}:</b> Main entry point for object design</li>
 * <li><b>{@link DesignContext}:</b> Specialized context for nested object configuration</li>
 * </ul>
 *
 * @param <T> the type of object being configured
 * @param <Context> the specific context type (enables method chaining with correct return type)
 * @see Designer
 * @see DesignContext
 * @see Factory#design(Class)
 */
@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
public abstract class DesignLanguage<T, Context extends DesignLanguage<T, Context>> {

    private final List<ObjectGenerator> generators = new ArrayList<>();
    private final List<Consumer<T>> processors = new ArrayList<>();

    DesignLanguage() {
    }

    abstract Context context();

    /**
     * Starts configuring a property of the object being designed using a method
     * reference.
     * <p>
     * This method begins the fluent configuration of a specific property by
     * accepting a method reference (getter) that identifies the property to be
     * configured. The method returns a {@link ParameterBinding} that allows you
     * to either set a specific value using {@code to()} or configure a nested
     * object using {@code withDesign()}.
     * </p>
     *
     * <p><b>Setting a Simple Value:</b></p>
     * <pre>
     * Factory
     *     .design(Product.class)
     *     .set(Product::name).to("Laptop")
     *     .set(Product::price).to(BigDecimal.valueOf(999.99))
     *     .create();
     * </pre>
     *
     * <p><b>Configuring a Nested Object:</b></p>
     * <pre>
     * Factory
     *     .design(Review.class)
     *     .set(Review::product).withDesign(product -> product
     *         .set(Product::name).to("Laptop")
     *         .set(Product::price).to(BigDecimal.valueOf(999.99))
     *     )
     *     .create();
     * </pre>
     *
     * <p><b>Multiple Property Configuration:</b></p>
     * <pre>
     * Factory
     *     .design(User.class)
     *     .set(User::firstName).to("John")
     *     .set(User::lastName).to("Doe")
     *     .set(User::email).to("john.doe@example.com")
     *     .create();
     * </pre>
     *
     * @param getterDelegate a method reference to the getter method of the
     *                       property to be configured (e.g., {@code Product::name})
     * @param <P>            the type of the property being configured
     * @return a {@link ParameterBinding} that allows setting the property value
     *         or configuring nested objects
     * @throws IllegalArgumentException if {@code getterDelegate} is {@code null}
     * @see ParameterBinding#to(Object)
     * @see ParameterBinding#withDesign(Function)
     */
    public <P> ParameterBinding<P> set(FunctionDelegate<T, P> getterDelegate) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' is null.");
        }

        return new ParameterBinding<>(getterDelegate);
    }

    /**
     * Adds a processing step to be applied to the object after it is created.
     * <p>
     * This method allows you to specify custom operations that will be executed
     * on the created object before it is returned. Processing steps are executed
     * in the order they are added, after the object has been generated with all
     * configured property values. This is useful for applying business logic,
     * calculations, or state changes that cannot be achieved through simple
     * property setting.
     * </p>
     *
     * <p><b>Single Processing Step:</b></p>
     * <pre>
     * Order order = Factory
     *     .design(Order.class)
     *     .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
     *     .process(o -> o.applyDiscount(BigDecimal.valueOf(10)))
     *     .create();
     * </pre>
     *
     * <p><b>Multiple Processing Steps:</b></p>
     * <pre>
     * Order order = Factory
     *     .design(Order.class)
     *     .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
     *     .process(o -> o.applyDiscount(BigDecimal.valueOf(10)))
     *     .process(o -> o.calculateTax())
     *     .process(o -> o.addShippingCost(BigDecimal.valueOf(5)))
     *     .create();
     * </pre>
     *
     * <p><b>Complex Processing:</b></p>
     * <pre>
     * User user = Factory
     *     .design(User.class)
     *     .set(User::firstName).to("John")
     *     .set(User::lastName).to("Doe")
     *     .process(u -> {
     *         u.setFullName(u.getFirstName() + " " + u.getLastName());
     *         u.setActive(true);
     *         u.setCreatedAt(LocalDateTime.now());
     *     })
     *     .create();
     * </pre>
     *
     * <p><b>Processing Order:</b></p>
     * <p>
     * Processing steps are applied in the exact order they are added:
     * </p>
     * <ol>
     * <li>Object is created with all configured property values</li>
     * <li>First processing step is applied</li>
     * <li>Second processing step is applied (if any)</li>
     * <li>Additional processing steps are applied in sequence</li>
     * <li>Final processed object is returned</li>
     * </ol>
     *
     * @param processor a consumer function that accepts the created object and
     *                  applies custom processing logic to it
     * @return the current context to enable method chaining
     * @throws IllegalArgumentException if {@code processor} is {@code null}
     * @see Designer#create()
     */
    public Context process(Consumer<T> processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        this.processors.add(processor);
        return context();
    }

    /**
     * A fluent interface for configuring a specific property of an object.
     * <p>
     * This class is created when you call {@link #set(FunctionDelegate)} and
     * provides two main ways to configure the property:
     * </p>
     * <ul>
     * <li><b>{@link #to(Object)}:</b> Set the property to a specific value</li>
     * <li><b>{@link #withDesign(Function)}:</b> Configure a nested object
     *     using the Designer API</li>
     * </ul>
     *
     * <p>
     * This class follows the builder pattern, allowing you to continue the
     * fluent configuration chain after setting the property value or
     * configuring nested objects.
     * </p>
     *
     * @param <P> the type of the property being configured
     * @see #to(Object)
     * @see #withDesign(Function)
     */
    public class ParameterBinding<P> {

        private final FunctionDelegate<T, P> getter;

        ParameterBinding(FunctionDelegate<T, P> getter) {
            this.getter = getter;
        }

        /**
         * Sets the property to a specific value.
         * <p>
         * This method assigns the given value to the property identified by
         * the method reference used in the {@link #set(FunctionDelegate)} call.
         * The value will be used when the object is created.
         * </p>
         *
         * <p><b>Basic Usage:</b></p>
         * <pre>
         * Product product = Factory
         *     .design(Product.class)
         *     .set(Product::name).to("Laptop")
         *     .set(Product::price).to(BigDecimal.valueOf(999.99))
         *     .create();
         * </pre>
         *
         * <p><b>Type Safety:</b></p>
         * <p>
         * The value type is checked at compile time to ensure it matches
         * the property type:
         * </p>
         * <pre>
         * // Correct - String value for String property
         * .set(Product::name).to("Laptop")
         *
         * // Correct - BigDecimal value for BigDecimal property
         * .set(Product::price).to(BigDecimal.valueOf(999.99))
         *
         * // Compile error - wrong type
         * .set(Product::name).to(123) // Cannot assign int to String property
         * </pre>
         *
         * @param value the value to assign to the property; must be compatible
         *              with the property type {@code P}
         * @return the current context to enable method chaining
         * @see #withDesign(Function)
         */
        public Context to(P value) {
            generators.add(ArgumentCustomizationDsl.set(getter).to(value));
            return context();
        }

        /**
         * Configures a nested object using the Designer API.
         * <p>
         * This method allows you to configure complex object hierarchies by
         * applying the Designer pattern to nested objects. The design function
         * receives a {@link DesignContext} instance for the nested object type
         * and must return the same instance to maintain the fluent chain.
         * </p>
         * <p>
         * The design function is executed lazily - it is not invoked when
         * {@code withDesign} is called, but only when the actual object
         * generation occurs. This enables more efficient object creation
         * and prevents unnecessary computations.
         * </p>
         *
         * <p><b>Single Level Nesting:</b></p>
         * <pre>
         * Review review = Factory
         *     .design(Review.class)
         *     .set(Review::product).withDesign(product -> product
         *         .set(Product::name).to("Laptop")
         *         .set(Product::price).to(BigDecimal.valueOf(999.99))
         *     )
         *     .set(Review::comment).to("Great product!")
         *     .create();
         * </pre>
         *
         * <p><b>Multi-Level Nesting:</b></p>
         * <pre>
         * Review review = Factory
         *     .design(Review.class)
         *     .set(Review::product).withDesign(product -> product
         *         .set(Product::name).to("Laptop")
         *         .set(Product::category).withDesign(category -> category
         *             .set(Category::name).to("Electronics")
         *             .set(Category::description).to("Electronic devices")
         *         )
         *     )
         *     .create();
         * </pre>
         *
         * <p><b>Design Function Requirements:</b></p>
         * <p>
         * The design function must return the same {@link DesignContext}
         * instance it receives:
         * </p>
         * <pre>
         * // Correct - returns the received parameter
         * .set(Review::product).withDesign(product -> product
         *     .set(Product::name).to("Laptop")
         * )
         *
         * // Wrong - returns null (throws IllegalArgumentException)
         * .set(Review::product).withDesign(product -> {
         *     product.set(Product::name).to("Laptop");
         *     return null; // This will cause an exception
         * })
         * </pre>
         *
         * @param design a function that configures the nested object using
         *               the Designer API; must return the received
         *               {@link DesignContext} instance
         * @return the current context to enable method chaining
         * @throws IllegalArgumentException if {@code design} is {@code null}
         *                                  or if the design function does not
         *                                  return its argument
         * @see DesignContext
         * @see #to(Object)
         */
        public Context withDesign(
            Function<DesignContext<P>, DesignContext<P>> design
        ) {
            if (design == null) {
                throw new IllegalArgumentException("The argument 'design' is null.");
            }

            generators.add(new LazyNestedDesignGenerator<>(getter, design));
            return context();
        }
    }

    private static final class LazyNestedDesignGenerator<T, P> implements ArgumentGenerator {

        private final Predicate<ParameterQuery> predicate;
        private final Function<DesignContext<P>, DesignContext<P>> design;

        LazyNestedDesignGenerator(
            FunctionDelegate<T, P> getter,
            Function<DesignContext<P>, DesignContext<P>> design
        ) {
            this.predicate = createParameterPredicate(getter);
            this.design = design;
        }

        private Predicate<ParameterQuery> createParameterPredicate(FunctionDelegate<T, P> getter) {
            // Use ArgumentCustomizationDsl logic directly to avoid duplication
            try {
                ArgumentGenerator tempGenerator =
                    (ArgumentGenerator) ArgumentCustomizationDsl.set(getter).to(null);
                return query -> {
                    ObjectContainer result = tempGenerator.generate(query, null);
                    return result != ObjectContainer.EMPTY;
                };
            } catch (Exception e) {
                return query -> false;
            }
        }

        @Override
        public ObjectContainer generate(ParameterQuery query, ResolutionContext context) {
            if (!predicate.test(query)) {
                return ObjectContainer.EMPTY;
            }

            // Lazily execute the design function when object generation is needed
            try {
                DesignContext<P> designContext = new DesignContext<>();
                DesignContext<P> result = design.apply(designContext);

                if (result != designContext) {
                    throw new RuntimeException("The design function must return its argument.", 
                        new IllegalArgumentException(
                            "The design function must return its argument."));
                }

                Class<?> nestedType = (Class<?>) query.getType();
                Factory<?> factory = Factory.create(nestedType);
                Customizer[] customizers = designContext.generators().toArray(Customizer[]::new);
                Object nestedObject = factory.get(customizers);
                return new ObjectContainer(nestedObject);
            } catch (RuntimeException e) {
                // Re-throw RuntimeException to propagate validation errors properly
                throw e;
            } catch (Exception e) {
                return ObjectContainer.EMPTY;
            }
        }
    }
}
