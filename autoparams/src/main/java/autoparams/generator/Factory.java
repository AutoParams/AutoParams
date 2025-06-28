package autoparams.generator;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.type.TypeReference;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * A factory for creating instances of a given type using a
 * {@link ResolutionContext}.
 * <p>
 * This class provides methods to generate objects of a specified type,
 * leveraging the configured context for customization and dependency
 * resolution. It supports both simple and generic type creation.
 * </p>
 *
 * <p><b>Examples:</b></p>
 * <pre>
 * // Create a Factory for MyClass
 * Factory&lt;MyClass&gt; factory = Factory.create(MyClass.class);
 *
 * // Create an instance of MyClass
 * MyClass instance = factory.get();
 *
 * // Create a list of 5 instances using getRange
 * List&lt;MyClass&gt; list1 = factory.getRange(5);
 *
 * // Create a stream of instances and collect 3
 * List&lt;MyClass&gt; list2 = factory.stream().limit(3).collect(Collectors.toList());
 * </pre>
 *
 * @param <T> the type of object this factory creates
 */
public final class Factory<T> implements Supplier<T> {

    private final ResolutionContext context;
    private final Type type;

    Factory(ResolutionContext context, Type type) {
        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' is null.");
        }

        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' is null.");
        }

        this.context = context;
        this.type = type;
    }

    private static <T> Class<?> inferType(T[] typeHint) {
        if (typeHint == null) {
            throw new IllegalArgumentException("The argument 'typeHint' is null.");
        } else if (typeHint.length > 0) {
            String message = "The argument 'typeHint' must be empty."
                + " It is used only to determine"
                + " the type of the object to be created.";
            throw new IllegalArgumentException(message);
        }

        Class<?> type = typeHint.getClass().getComponentType();
        boolean isGeneric = type.getTypeParameters().length > 0;
        if (isGeneric) {
            String message = "To resolve an object of a generic class,"
                + " use the method"
                + " 'create(ResolutionContext, TypeReference<T>)' instead.";
            throw new IllegalArgumentException(message);
        }

        return type;
    }

    /**
     * Creates a {@link Factory} instance for the type inferred from the
     * generic parameter.
     * <p>
     * You do not need to specify the array parameter explicitly; the compiler
     *  handles type inference automatically.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a Factory for MyClass using type inference
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * MyClass instance = factory.get();
     * </pre>
     *
     * @param typeHint an empty vararg used only for type inference; should not
     *                 be provided explicitly
     * @param <T>      the type of object to be created by the {@link Factory}
     * @return a {@link Factory} instance for the inferred type
     * @throws IllegalArgumentException if typeHint is null, not empty, or is
     *                                  an array of a generic type
     */
    @SuppressWarnings("unchecked")
    public static <T> Factory<T> create(T... typeHint) {
        return (Factory<T>) create(inferType(typeHint));
    }

    /**
     * Creates a {@link Factory} instance for the type inferred from the generic
     * parameter, using the provided {@link ResolutionContext}.
     * <p>
     * You do not need to specify the array parameter explicitly; the compiler
     *  handles type inference automatically.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Create a Factory for MyClass using type inference and a custom context
     * Factory&lt;MyClass&gt; factory = Factory.create(context);
     * MyClass instance = factory.get();
     * </pre>
     *
     * @param context  the resolution context to use
     * @param typeHint an empty vararg used only for type inference; should not
     *                 be provided explicitly
     * @param <T>      the type of object to be created by the {@link Factory}
     * @return a {@link Factory} instance for the inferred type
     * @throws IllegalArgumentException if typeHint is null, not empty, or is an
     *                                  array of a generic type
     */
    @SuppressWarnings("unchecked")
    public static <T> Factory<T> create(
        ResolutionContext context,
        T... typeHint
    ) {
        return (Factory<T>) create(context, inferType(typeHint));
    }

    /**
     * Creates a {@link Factory} instance for the specified class type.
     * <p>
     * This method is useful when you want to explicitly provide the class type
     * for which the factory should generate instances.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a Factory for MyClass
     * Factory&lt;MyClass&gt; factory = Factory.create(MyClass.class);
     * MyClass instance = factory.get();
     * </pre>
     *
     * @param type the class type for which to create the factory
     * @param <T>  the type of object to be created by the {@link Factory}
     * @return a {@link Factory} instance for the specified type
     * @throws IllegalArgumentException if {@code type} is {@code null}
     */
    public static <T> Factory<T> create(Class<T> type) {
        return new Factory<>(new ResolutionContext(), type);
    }

    /**
     * Creates a {@link Factory} instance for the specified class type using the
     * given {@link ResolutionContext}.
     * <p>
     * Use this method when you want to explicitly provide both the resolution
     * context and the class type for which the factory should generate
     * instances.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Create a Factory for MyClass with a custom context
     * Factory&lt;MyClass&gt; factory = Factory.create(context, MyClass.class);
     * MyClass instance = factory.get();
     * </pre>
     *
     * @param context the resolution context to use
     * @param type    the class type for which to create the factory
     * @param <T>     the type of object to be created by the {@link Factory}
     * @return a {@link Factory} instance for the specified type
     * @throws IllegalArgumentException if {@code context} or {@code type} is
     *                                  {@code null}
     */
    public static <T> Factory<T> create(
        ResolutionContext context,
        Class<T> type
    ) {
        return new Factory<>(context, type);
    }

    /**
     * Creates a {@link Factory} instance for the specified generic type using a
     * {@link TypeReference}.
     * <p>
     * Use this method when you want to create a factory for a generic type,
     * such as {@link List}&lt;{@link String}&gt;.
     * </p>
     *
     * @param typeReference the type reference describing the generic type
     * @param <T>           the type of object to be created by {@link Factory}
     * @return a {@link Factory} instance for the specified generic type
     */
    public static <T> Factory<T> create(TypeReference<T> typeReference) {
        return new Factory<>(new ResolutionContext(), typeReference.getType());
    }

    /**
     * Creates a {@link Factory} instance for the specified generic type using
     * the given {@link ResolutionContext} and {@link TypeReference}.
     * <p>
     * Use this method when you want to create a factory for a generic type
     * (such as {@link List}&lt;{@link String}&gt;) with a custom resolution
     * context.
     * </p>
     *
     * @param context       the resolution context to use
     * @param typeReference the type reference describing the generic type
     * @param <T>           the type of object to be created by {@link Factory}
     * @return a {@link Factory} instance for the specified generic type
     */
    public static <T> Factory<T> create(
        ResolutionContext context,
        TypeReference<T> typeReference
    ) {
        return new Factory<>(context, typeReference.getType());
    }

    /**
     * Returns a new instance of the type managed by this factory.
     * <p>
     * This method generates a new object of type {@code T} using the configured
     * {@link ResolutionContext}.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * MyClass instance = factory.get();
     * </pre>
     *
     * @return a new instance of type {@code T}
     */
    @Override
    public T get() {
        return get(context);
    }

    /**
     * Returns a new instance of the type managed by this factory, applying the
     * given customizers.
     * <p>
     * This method generates a new object of type {@code T} using the configured
     * {@link ResolutionContext}, with the specified {@link Customizer}
     * instances applied for customization.
     * </p>
     *
     * <p>
     * <b>Note:</b> The provided customizers only affect the generated instance
     * and do not modify the state of this {@link Factory} itself.
     * </p>
     *
     * <p><b>Examples:</b></p>
     * <pre>
     * // Custom ObjectGenerator that always returns "hello world" for String type
     * public class StringGenerator extends ObjectGeneratorBase&lt;String&gt; {
     *
     *     &#64;Override
     *     protected String generateObject(ObjectQuery query, ResolutionContext context) {
     *         return "hello world";
     *     }
     * }
     * </pre>
     *
     * <pre>
     * // Custom ObjectProcessor that logs processed values
     * public class LoggingProcessor implements ObjectProcessor {
     *
     *     &#64;Override
     *     public ObjectProcessor customize(ObjectProcessor processor) {
     *         return (query, value, context) -> {
     *             System.out.println("Processing value: " + value);
     *             processor.process(query, value, context);
     *         };
     *     }
     * }
     * </pre>
     *
     * <pre>
     * Factory&lt;String&gt; factory = Factory.create();
     *
     * // Create a String instance by applying StringGenerator and LoggingProcessor customizers
     * String value = factory.get(new StringGenerator(), new LoggingProcessor());
     * </pre>
     *
     * <p><b>Example using DSL:</b></p>
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
     * import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
     *
     * public class TestClass {
     *     void testDsl(Product product, int rating) {
     *         Factory&lt;Review&gt; factory = Factory.create();
     *         Review review = factory.get(
     *             set(Review::getProduct).to(product),
     *             set(Review::getRating).to(rating)
     *         );
     *         assertSame(product, review.getProduct());
     *         assertEquals(rating, review.getRating());
     *     }
     * }
     * </pre>
     *
     * @param customizers customizers to apply to the generated instance (do not
     *                    affect this Factory)
     * @return a new instance of type {@code T} with the customizations applied
     */
    public T get(Customizer... customizers) {
        ResolutionContext context = customizers.length == 0
            ? this.context
            : this.context.branch(customizers);
        return get(context);
    }

    @SuppressWarnings("unchecked")
    private T get(ResolutionContext context) {
        return (T) context.resolve(new DefaultObjectQuery(type));
    }

    /**
     * Returns a sequential {@link Stream} of instances of the type managed by
     * this factory.
     * <p>
     * This method generates an infinite stream of objects of type {@code T}.
     * You may optionally specify one or more {@link Customizer} instances to
     * customize each generated object, but providing customizers is not
     * required. The stream can be limited using standard stream operations such
     * as {@link Stream#limit}.
     * </p>
     *
     * <p>
     * <b>Note:</b> The provided customizers only affect the generated instances
     * in the stream and do not modify the state of this {@link Factory} itself.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * List&lt;MyClass&gt; list = factory.stream().limit(3).collect(Collectors.toList());
     * </pre>
     *
     * <p><b>Example with customizers:</b></p>
     * <pre>
     * import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
     *
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * Customizer customizer = set(MyClass::getName).to("example");
     * List&lt;MyClass&gt; list = factory.stream(customizer).limit(2).collect(Collectors.toList());
     * assertEquals("example", list.get(0).getName());
     * </pre>
     *
     * @param customizers customizers to apply to each generated instance
     *                    (optional; does not affect this Factory)
     * @return a sequential {@link Stream} of instances of type {@code T}
     */
    public Stream<T> stream(Customizer... customizers) {
        Factory<T> factory = customizers.length == 0
            ? this
            : new Factory<>(context.branch(customizers), type);
        return Stream.generate(factory);
    }

    /**
     * Returns an unmodifiable {@link List} containing a specified number of
     * instances of the type managed by this factory.
     * <p>
     * This method generates a list of {@code size} objects of type {@code T}.
     * You may optionally specify one or more {@link Customizer} instances to
     * customize each generated object, but providing customizers is not
     * required.
     * </p>
     *
     * <p>
     * <b>Note:</b> The provided customizers only affect the generated instances
     * in the list and do not modify the state of this {@link Factory} itself.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * List&lt;MyClass&gt; list = factory.getRange(3);
     * </pre>
     *
     * <p><b>Example with customizers:</b></p>
     * <pre>
     * import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
     *
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * Customizer customizer = set(MyClass::getName).to("example");
     * List&lt;MyClass&gt; list = factory.getRange(2, customizer);
     * assertEquals("example", list.get(0).getName());
     * </pre>
     *
     * @param size        the number of instances to generate
     * @param customizers customizers to apply to each generated instance
     *                    (optional; does not affect this Factory)
     * @return an unmodifiable {@link List} of instances of type {@code T}
     */
    public List<T> getRange(int size, Customizer... customizers) {
        List<T> results = stream(customizers).limit(size).collect(toList());
        return unmodifiableList(results);
    }

    /**
     * Applies the given {@link Customizer} to this factory's
     * {@link ResolutionContext}.
     * <p>
     * This method modifies the internal state of the factory so that the
     * provided customizer will affect all subsequently generated instances from
     * this factory.
     * </p>
     *
     * @param customizer the customizer to apply to this factory's context
     */
    public void applyCustomizer(Customizer customizer) {
        context.applyCustomizer(customizer);
    }

    /**
     * Applies the given {@link Customizer} instances to this factory's
     * {@link ResolutionContext}.
     * <p>
     * This method modifies the internal state of the factory so that the
     * provided customizers will affect all subsequently generated instances
     * from this factory.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
     *
     * Factory&lt;MyClass&gt; factory = Factory.create();
     * factory.customize(
     *     set(MyClass::getName).to("example"),
     *     set(MyClass::getAge).to(42)
     * );
     * MyClass instance = factory.get();
     * assertEquals("example", instance.getName());
     * assertEquals(42, instance.getAge());
     * </pre>
     *
     * @param customizers the customizers to apply to this factory's context
     */
    public void customize(Customizer... customizers) {
        context.customize(customizers);
    }

    /**
     * Creates a {@link Designer} instance for fluent object configuration of
     * the specified type.
     * <p>
     * The Designer API provides a fluent interface for configuring and creating
     * objects with specific property values and nested object relationships.
     * This is particularly useful for test data generation where you need to
     * control specific aspects of object creation.
     * </p>
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
     * <p><b>Nested Object Configuration:</b></p>
     * <pre>
     * Review review = Factory
     *     .design(Review.class)
     *     .set(Review::product).withDesign(product -> product
     *         .set(Product::name).to("Product A")
     *         .set(Product::price).to(BigDecimal.valueOf(100))
     *     )
     *     .set(Review::comment).to("Great product!")
     *     .create();
     * </pre>
     *
     * <p><b>Object Processing:</b></p>
     * <pre>
     * Order order = Factory
     *     .design(Order.class)
     *     .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
     *     .process(o -> o.applyDiscount(BigDecimal.valueOf(10)))
     *     .create();
     * </pre>
     *
     * @param type the class type for which to create the designer
     * @param <T>  the type of object to be configured and created
     * @return a {@link Designer} instance for fluent object configuration
     * @throws IllegalArgumentException if {@code type} is {@code null}
     * @see Designer
     * @see Designer#set(autoparams.customization.dsl.FunctionDelegate)
     * @see Designer#process(java.util.function.Consumer)
     * @see Designer#create()
     */
    public static <T> Designer<T> design(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' is null.");
        }

        Factory<T> factory = create(type);
        return new Designer<>(factory);
    }

    /**
     * Creates a {@link Designer} instance for fluent object configuration of
     * the specified type using a custom {@link ResolutionContext}.
     * <p>
     * This method creates a Designer that uses the provided ResolutionContext
     * for object generation, allowing you to customize the object creation
     * process while still benefiting from the fluent configuration API.
     * The ResolutionContext enables dependency injection, custom generators,
     * and other advanced configuration scenarios.
     * </p>
     *
     * <p><b>Basic Usage with Custom Context:</b></p>
     * <pre>
     * &#64;Test
     * &#64;AutoParams
     * void testMethod(&#64;Freeze BigDecimal price, ResolutionContext context) {
     *     Product product = Factory
     *         .design(context, Product.class)
     *         .set(Product::name).to("Laptop")
     *         .create();
     *
     *     // The price will be the frozen value from the context
     *     assertThat(product.price()).isEqualTo(price);
     * }
     * </pre>
     *
     * <p><b>Advanced Customization:</b></p>
     * <pre>
     * ResolutionContext context = new ResolutionContext();
     * context.customize(new MyCustomGenerator());
     *
     * Order order = Factory
     *     .design(context, Order.class)
     *     .set(Order::getStatus).to(OrderStatus.PENDING)
     *     .process(o -> o.calculateTotals())
     *     .create();
     * </pre>
     *
     * @param context the resolution context to use for object generation
     * @param type    the class type for which to create the designer
     * @param <T>     the type of object to be configured and created
     * @return a {@link Designer} instance using the specified context
     * @throws IllegalArgumentException if {@code context} or {@code type} is {@code null}
     * @see Designer
     * @see ResolutionContext
     * @see #design(Class)
     */
    @Deprecated
    public static <T> Designer<T> design(
        ResolutionContext context,
        Class<T> type
    ) {
        if (context == null) {
            throw new IllegalArgumentException("The argument 'context' is null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' is null.");
        }

        Factory<T> factory = create(context, type);
        return new Designer<>(factory);
    }
}
