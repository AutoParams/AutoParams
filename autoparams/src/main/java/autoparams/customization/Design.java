package autoparams.customization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.dsl.FunctionDelegate;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.internal.reflect.Property;
import autoparams.type.TypeReference;
import lombok.AllArgsConstructor;

import static java.util.Collections.unmodifiableList;

/**
 * A fluent API for configuring object creation and property customization.
 * <p>
 * The {@code Design} class provides a declarative way to specify how objects
 * should be created and configured during test execution. It implements the
 * {@link Customizer} interface, allowing Design instances to be used directly
 * with {@link ResolutionContext} for seamless integration with the AutoParams
 * framework.
 * </p>
 *
 * <p><b>Basic Usage:</b></p>
 * <pre>
 * Product product = Design.of(Product.class)
 *     .set(Product::name, "Test Product")
 *     .set(Product::stockQuantity, 100)
 *     .instantiate();
 * </pre>
 *
 * <p><b>Generic Type Support:</b></p>
 * <pre>
 * Design&lt;Container&lt;Product&gt;&gt; design =
 *     Design.of(new TypeReference&lt;Container&lt;Product&gt;&gt;() { });
 * Container&lt;Product&gt; container = design.instantiate();
 * </pre>
 *
 * <p><b>ResolutionContext Integration:</b></p>
 * <pre>
 * Design&lt;Product&gt; design = Design.of(Product.class)
 *     .set(Product::stockQuantity, 100);
 *
 * ResolutionContext context = new ResolutionContext();
 * context.customize(design);
 *
 * Product product = context.resolve(Product.class);
 * </pre>
 *
 * @param <T> the type of object this design configures
 *
 * @see Customizer
 * @see ResolutionContext
 * @see TypeReference
 */
public class Design<T> implements Customizer {

    private final Type type;
    private final Customizer[] customizers;

    private Design(Type type) {
        this.type = type;
        this.customizers = new Customizer[0];
    }

    private Design(Type type, Customizer[] customizers) {
        this.type = type;
        this.customizers = customizers;
    }

    /**
     * Creates a new Design instance for the specified type.
     * <p>
     * This is the starting point for building a fluent object configuration.
     * The returned Design can be further configured using
     * {@link #set(FunctionDelegate, Object)},
     * {@link #supply(FunctionDelegate, Supplier)}, and
     * {@link #design(FunctionDelegate, Function)} methods.
     * </p>
     *
     * @param <T> the type of object to design
     * @param type the {@code Class} object representing the type to configure
     * @return a new Design instance for the specified type
     * @throws IllegalArgumentException if {@code type} is null
     */
    public static <T> Design<T> of(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null");
        }

        return new Design<>(type);
    }

    /**
     * Creates a new Design instance for the specified generic type.
     * <p>
     * This method allows creating Design instances for generic types by using
     * {@link TypeReference} to capture the full generic type information at
     * compile time. This is particularly useful for parameterized types where
     * the type parameter information would otherwise be lost due to type erasure.
     * </p>
     * <p>
     * The returned Design can be further configured using
     * {@link #set(FunctionDelegate, Object)},
     * {@link #supply(FunctionDelegate, Supplier)}, and
     * {@link #design(FunctionDelegate, Function)} methods.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a Design for a generic type
     * Design&lt;Container&lt;Product&gt;&gt; design =
     *     Design.of(new TypeReference&lt;Container&lt;Product&gt;&gt;() { });
     * Container&lt;Product&gt; container = design.instantiate();
     * </pre>
     *
     * @param <T> the type of object to design
     * @param typeReference the {@link TypeReference} capturing the generic type information
     * @return a new Design instance for the specified generic type
     * @throws IllegalArgumentException if {@code typeReference} is null
     * @see TypeReference
     */
    public static <T> Design<T> of(TypeReference<T> typeReference) {
        if (typeReference == null) {
            throw new IllegalArgumentException("The argument 'typeReference' must not be null");
        }

        return new Design<>(typeReference.getType());
    }

    /**
     * Configures a property to be supplied by the given {@link Supplier}.
     * <p>
     * This method allows dynamic property value generation during object
     * instantiation. The supplier will be called each time an object is created.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Random random = new Random();
     * Product product = Design.of(Product.class)
     *     .supply(Product::stockQuantity, () -&gt; random.nextInt(100))
     *     .instantiate();
     * </pre>
     *
     * @param <P> the type of the property
     * @param getterDelegate a function delegate representing the property getter
     * @param supplier the supplier that provides property values
     * @return a new Design instance with the property configuration added
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
        Customizer customizer = new ArgumentSupplier<>(property, supplier);
        return new Design<>(type, append(customizers, customizer));
    }

    private static <E> E[] append(E[] source, E element) {
        E[] result = Arrays.copyOf(source, source.length + 1);
        result[source.length] = element;
        return result;
    }

    /**
     * Configures a property to have a fixed value.
     * <p>
     * This is a convenience method that sets a property to a constant value.
     * It is equivalent to calling {@link #supply(FunctionDelegate, Supplier)}
     * with a supplier that always returns the same value.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Product product = Design.of(Product.class)
     *     .set(Product::name, "Test Product")
     *     .set(Product::stockQuantity, 100)
     *     .instantiate();
     * </pre>
     *
     * @param <P> the type of the property
     * @param getterDelegate a function delegate representing the property getter
     * @param value the fixed value to assign to the property
     * @return a new Design instance with the property configuration added
     * @throws IllegalArgumentException if {@code getterDelegate} is null
     */
    public <P> Design<T> set(FunctionDelegate<T, P> getterDelegate, P value) {
        return supply(getterDelegate, () -> value);
    }

    /**
     * Configures a nested property using a nested Design configuration.
     * <p>
     * This method allows configuring complex object properties by applying
     * a design function to create and configure the nested object. The design
     * function receives a new Design instance for the property type and returns
     * a configured Design that will be instantiated for the property value.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Product product = Design.of(Product.class)
     *     .design(Product::category, category -&gt; category
     *         .set(Category::name, "Electronics")
     *         .set(Category::description, "Electronic products")
     *     )
     *     .instantiate();
     * </pre>
     *
     * @param <P> the type of the nested property
     * @param getterDelegate a function delegate representing the property getter
     * @param designFunction a function that configures the nested object design
     * @return a new Design instance with the nested property configuration added
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
     * Creates a new instance of the configured type with all design settings applied.
     *
     * @return a new instance of type {@code T} with all configured properties applied
     */
    public T instantiate() {
        ResolutionContext context = new ResolutionContext();
        context.customize(customizers);
        return (T) context.resolve(new DefaultObjectQuery(type));
    }

    /**
     * Creates a new instance using the provided {@link ResolutionContext}.
     *
     * @param context the resolution context to use for object creation
     * @return a new instance of type {@code T} with all configured properties applied
     * @throws IllegalArgumentException if {@code context} is null
     */
    public T instantiate(ResolutionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers);
        return (T) branch.resolve(new DefaultObjectQuery(type));
    }

    /**
     * Creates multiple instances of the configured type.
     *
     * @param count the number of instances to create
     * @return an unmodifiable list containing the created instances
     * @throws IllegalArgumentException if {@code count} is less than 0
     */
    public List<T> instantiate(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(instantiate());
        }
        return unmodifiableList(result);
    }

    /**
     * Creates multiple instances using the provided {@link ResolutionContext}.
     *
     * @param count the number of instances to create
     * @param context the resolution context to use for object creation
     * @return an unmodifiable list containing the created instances
     * @throws IllegalArgumentException if {@code count} is less than 0 or {@code context} is null
     */
    public List<T> instantiate(int count, ResolutionContext context) {
        if (count < 0) {
            throw new IllegalArgumentException("The argument 'count' must not be less than 0");
        }

        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' must not be null");
        }

        ResolutionContext branch = context.branch();
        branch.customize(customizers);
        List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add((T) branch.resolve(new DefaultObjectQuery(type)));
        }
        return unmodifiableList(result);
    }

    /**
     * Customizes the provided {@link ObjectGenerator} with this design's configurations.
     * <p>
     * This method implements the {@link Customizer} interface, allowing Design instances
     * to be used directly with {@link ResolutionContext} and other AutoParams framework
     * components.
     * </p>
     *
     * @param generator the object generator to customize
     * @return a customized object generator that applies this design's configurations
     * @throws IllegalArgumentException if {@code generator} is null
     */
    @SuppressWarnings("GrazieInspection")
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        return new CompositeCustomizer(customizers).customize(generator);
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
