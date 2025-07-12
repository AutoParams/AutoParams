package autoparams.customization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.dsl.FunctionDelegate;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.internal.reflect.Property;
import lombok.AllArgsConstructor;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Provides a fluent API for configuring and creating objects with specific
 * property values. {@link Design} instances are immutable and thread-safe, with each
 * configuration method returning a new {@link Design} instance.
 *
 * <p><b>Basic Usage Example:</b></p>
 * <p>Simple object creation with property configuration:</p>
 * <pre>
 * Product product = Design.of(Product.class)
 *     .set(Product::name, "Test Product")
 *     .set(Product::price, 99.99)
 *     .instantiate();
 * </pre>
 *
 * <p><b>Advanced Usage Example:</b></p>
 * <p>Nested object configuration with dynamic values:</p>
 * <pre>
 * Order order = Design.of(Order.class)
 *     .supply(Order::orderDate, () -&gt; Instant.now())
 *     .design(Order::customer, customer -&gt; customer
 *         .set(Customer::name, "John Doe")
 *         .set(Customer::email, "john&#64;example.com"))
 *     .instantiate();
 * </pre>
 *
 * <p>{@link Design} instances implement {@link ObjectGenerator} and can be used as
 * reusable customizers within the AutoParams framework. Property references
 * use method references for type safety and compile-time validation.</p>
 *
 * @param <T> the type of object to design and create
 *
 * @see ObjectGenerator
 * @see Customizer
 * @see ResolutionContext
 */
public class Design<T> implements ObjectGenerator {

    private final Class<T> type;
    private final List<Customizer> customizers;

    private Design(Class<T> type) {
        this.type = type;
        this.customizers = emptyList();
    }

    private Design(Class<T> type, List<Customizer> customizers) {
        this.type = type;
        this.customizers = customizers;
    }

    /**
     * Creates a new {@link Design} instance for the specified type.
     *
     * <p><b>Example:</b></p>
     * <p>Creating and configuring a Product using method chaining:</p>
     * <pre>
     * Product product = Design.of(Product.class)
     *     .set(Product::name, "Sample Product")
     *     .instantiate();
     * </pre>
     *
     * @param <T> the type of object to design
     * @param type the class of the object to design
     * @return a new {@link Design} instance for the specified type
     * @throws IllegalArgumentException if {@code type} is null
     */
    public static <T> Design<T> of(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null");
        }

        return new Design<>(type);
    }

    /**
     * Configures a property to be supplied by a lazy-evaluated supplier.
     * Returns a new {@link Design} instance with the property configuration added.
     *
     * <p><b>Example:</b></p>
     * <p>Using a supplier for dynamic timestamp generation:</p>
     * <pre>
     * Order order = Design.of(Order.class)
     *     .supply(Order::id, () -&gt; UUID.randomUUID().toString())
     *     .supply(Order::createdAt, () -&gt; Instant.now())
     *     .instantiate();
     * </pre>
     *
     * <p>The supplier is evaluated each time an instance is created, enabling
     * dynamic value generation. Property references use method references for
     * type safety.</p>
     *
     * @param <P> the type of the property
     * @param getterDelegate a method reference to the property getter
     * @param supplier a supplier that provides the property value
     * @return a new {@link Design} instance with the property configuration added
     * @throws IllegalArgumentException if {@code getterDelegate} or {@code supplier} is null
     */
    public <P> Design<T> supply(
        FunctionDelegate<T, P> getterDelegate,
        Supplier<P> supplier
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' must not be null");
        }

        if (supplier == null) {
            throw new IllegalArgumentException("The argument 'supplier' must not be null");
        }

        Property<T, P> property = Property.parse(getterDelegate);
        List<Customizer> nextCustomizers = new ArrayList<>(customizers);
        nextCustomizers.add(new ArgumentSupplier<>(property, supplier));
        return new Design<>(type, unmodifiableList(nextCustomizers));
    }

    /**
     * Configures a property with a fixed value. This is a convenience method
     * that delegates to {@link #supply(FunctionDelegate, java.util.function.Supplier)}.
     *
     * <p><b>Example:</b></p>
     * <p>Setting fixed property values:</p>
     * <pre>
     * Product product = Design.of(Product.class)
     *     .set(Product::name, "Test Product")
     *     .set(Product::price, 99.99)
     *     .set(Product::available, true)
     *     .instantiate();
     * </pre>
     *
     * <p>Property references use method references for type safety and
     * compile-time validation.</p>
     *
     * @param <P> the type of the property
     * @param getterDelegate a method reference to the property getter
     * @param value the fixed value to set for the property
     * @return a new {@link Design} instance with the property configuration added
     * @throws IllegalArgumentException if {@code getterDelegate} is null
     */
    public <P> Design<T> set(FunctionDelegate<T, P> getterDelegate, P value) {
        return supply(getterDelegate, () -> value);
    }

    /**
     * Configures a nested object property using another {@link Design} instance.
     * This enables fluent configuration of complex object hierarchies.
     *
     * <p><b>Example:</b></p>
     * <p>Configuring a nested customer object within an order:</p>
     * <pre>
     * Order order = Design.of(Order.class)
     *     .set(Order::orderNumber, "ORD-12345")
     *     .design(Order::customer, customer -&gt; customer
     *         .set(Customer::name, "John Doe")
     *         .set(Customer::email, "john&#64;example.com"))
     *     .instantiate();
     * </pre>
     *
     * <p>The design function receives a new {@link Design} instance for the property
     * type and should return a configured {@link Design} instance. The nested object
     * is instantiated when the parent object is created.</p>
     *
     * @param <P> the type of the nested property
     * @param getterDelegate a method reference to the property getter
     * @param designFunction a function that configures the nested object {@link Design}
     * @return a new {@link Design} instance with the nested object configuration added
     * @throws IllegalArgumentException if {@code getterDelegate} or {@code designFunction} is null
     */
    public <P> Design<T> design(
        FunctionDelegate<T, P> getterDelegate,
        Function<Design<P>, Design<P>> designFunction
    ) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' must not be null");
        }

        if (designFunction == null) {
            throw new IllegalArgumentException("The argument 'designFunction' must not be null");
        }

        return supply(getterDelegate, () -> {
            Property<T, P> property = Property.parse(getterDelegate);
            Design<P> design = Design.of(property.getType());
            return designFunction.apply(design).instantiate();
        });
    }

    /**
     * Creates a single instance of the configured object using a new
     * {@link ResolutionContext}.
     *
     * <p><b>Example:</b></p>
     * <p>Creating an instance with configured properties using method chaining:</p>
     * <pre>
     * Product product = Design.of(Product.class)
     *     .set(Product::name, "Test Product")
     *     .set(Product::price, 99.99)
     *     .instantiate();
     * </pre>
     *
     * @return a new instance with all configured properties applied
     */
    public T instantiate() {
        return instantiate(new ResolutionContext());
    }

    /**
     * Creates a single instance of the configured object using the provided
     * {@link ResolutionContext}.
     *
     * <p><b>Example:</b></p>
     * <p>Creating an instance with custom ResolutionContext:</p>
     * <pre>
     * ResolutionContext context = new ResolutionContext();
     * context.customize(new MyCustomizer());
     *
     * Product product = Design.of(Product.class)
     *     .set(Product::name, "Test Product")
     *     .instantiate(context);
     * </pre>
     *
     * @param context the {@link ResolutionContext} to use for object creation
     * @return a new instance with all configured properties applied
     * @throws IllegalArgumentException if {@code context} is null
     */
    public T instantiate(ResolutionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers.toArray(new Customizer[0]));
        return branch.resolve(type);
    }

    /**
     * Creates multiple instances of the configured object using a new
     * {@link ResolutionContext}. Returns an unmodifiable list of instances.
     *
     * <p><b>Example:</b></p>
     * <p>Creating multiple instances with method chaining:</p>
     * <pre>
     * List&lt;Product&gt; products = Design.of(Product.class)
     *     .set(Product::category, "Electronics")
     *     .supply(Product::name, () -&gt; "Product-" + UUID.randomUUID())
     *     .instantiate(5);
     * </pre>
     *
     * @param count the number of instances to create
     * @return an unmodifiable list of instances with configured properties
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public List<T> instantiate(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        return instantiate(count, new ResolutionContext());
    }

    /**
     * Creates multiple instances of the configured object using the provided
     * {@link ResolutionContext}. Returns an unmodifiable list of instances.
     *
     * <p><b>Example:</b></p>
     * <p>Creating multiple instances with custom ResolutionContext:</p>
     * <pre>
     * ResolutionContext context = new ResolutionContext();
     * context.customize(new MyCustomizer());
     *
     * List&lt;Product&gt; products = Design.of(Product.class)
     *     .set(Product::category, "Electronics")
     *     .instantiate(3, context);
     * </pre>
     *
     * @param count the number of instances to create
     * @param context the {@link ResolutionContext} to use for object creation
     * @return an unmodifiable list of instances with configured properties
     * @throws IllegalArgumentException if {@code count} is negative or {@code context} is null
     */
    public List<T> instantiate(int count, ResolutionContext context) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers.toArray(new Customizer[0]));

        List<T> instances = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            instances.add(branch.resolve(type));
        }

        return unmodifiableList(instances);
    }

    /**
     * Generates an object using the {@link ObjectGenerator} interface. This method
     * enables {@link Design} instances to be used as reusable customizers within
     * the AutoParams framework.
     *
     * @param query the object query specifying the type to generate
     * @param context the resolution context for object generation
     * @return an {@link ObjectContainer} with the generated instance if the query
     *         type matches, otherwise {@link ObjectContainer#EMPTY}
     * @throws IllegalArgumentException if {@code query} or {@code context} is null
     */
    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query == null) {
            throw new IllegalArgumentException("The argument 'query' must not be null");
        }

        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        return query.getType().equals(type)
            ? new ObjectContainer(instantiate(context))
            : ObjectContainer.EMPTY;
    }

    @AllArgsConstructor
    private static class ArgumentSupplier<T, P> implements ObjectGenerator {

        private final Property<T, P> property;
        private final Supplier<P> supplier;

        @Override
        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return matches(query)
                ? new ObjectContainer(supplier.get())
                : ObjectContainer.EMPTY;
        }

        private boolean matches(ObjectQuery query) {
            return query instanceof ParameterQuery
                && matches((ParameterQuery) query);
        }

        private boolean matches(ParameterQuery query) {
            return matchesParameterType(query)
                && matchesParameterName(query)
                && matchesDeclaringClass(query);
        }

        private boolean matchesParameterType(ParameterQuery query) {
            return property.getType().equals(query.getType());
        }

        private boolean matchesParameterName(ParameterQuery query) {
            return property.getName().equals(query.getRequiredParameterName());
        }

        private boolean matchesDeclaringClass(ParameterQuery query) {
            return property.getDeclaringClass().equals(
                query
                    .getParameter()
                    .getDeclaringExecutable()
                    .getDeclaringClass()
            );
        }
    }
}
