package autoparams;

import java.lang.reflect.Type;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.UnwrapFailedException;
import autoparams.processor.ObjectProcessor;
import autoparams.type.TypeReference;

/**
 * Provides context for object resolution in AutoParams.
 * <p>
 * This class manages the object generator and processor used to create and
 * customize objects. It offers methods to resolve objects by type, apply
 * customizers, and create branched contexts with additional customization.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * // Create a new resolution context
 * ResolutionContext context = new ResolutionContext();
 *
 * // Resolve an instance of a specific class
 * MyClass instance1 = context.resolve(MyClass.class);
 *
 * // Usage with type inference without specifying the class explicitly
 * MyClass instance2 = context.resolve();
 * </pre>
 */
public class ResolutionContext {

    private ObjectGenerator generator;
    private ObjectProcessor processor;

    /**
     * Creates a new {@link ResolutionContext} with the specified object
     * generator and processor.
     * <p>
     * This constructor allows for custom configuration of the context by
     * providing specific implementations of {@link ObjectGenerator} and
     * {@link ObjectProcessor}.
     * </p>
     *
     * @param generator the object generator to use for object creation
     * @param processor the object processor to use for post-processing
     *                  generated objects
     * @throws IllegalArgumentException if {@code generator} or
     *                                  {@code processor} is {@code null}
     */
    public ResolutionContext(
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        this.generator = generator;
        this.processor = processor;
    }

    /**
     * Creates a new {@link ResolutionContext} with the default object generator
     * and processor.
     * <p>
     * This constructor initializes the context using the default
     * implementations of {@link ObjectGenerator} and {@link ObjectProcessor},
     * providing a standard configuration for object resolution and
     * customization.
     * </p>
     */
    public ResolutionContext() {
        this(ObjectGenerator.DEFAULT, ObjectProcessor.DEFAULT);
    }

    /**
     * Resolves an object of the inferred type using type inference.
     * <p>
     * This method allows you to resolve an object without explicitly specifying
     * the class type, by passing an empty vararg parameter. The type is
     * inferred from the generic parameter.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Resolve an instance of MyClass using type inference
     * MyClass instance = context.resolve();
     * </pre>
     *
     * @param typeHint an empty vararg used only for type inference; should not
     *                 be provided explicitly
     * @param <T>      the type of the object to resolve
     * @return an instance of the inferred type
     * @throws IllegalArgumentException if {@code typeHint} is null or not
     *                                  empty, or if the inferred type is
     *                                  generic
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> T resolve(T... typeHint) {
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
                + " use the method 'resolve(TypeReference<T>)' instead.";
            throw new IllegalArgumentException(message);
        }

        return (T) resolve(type);
    }

    /**
     * Resolves an instance of the specified class type.
     * <p>
     * This method creates an {@link ObjectQuery} for the given class type and
     * resolves an object using the current {@link ResolutionContext}.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Resolve an instance of MyClass
     * MyClass instance = context.resolve(MyClass.class);
     * </pre>
     *
     * @param type the class type to resolve
     * @param <T>  the type of the object to resolve
     * @return an instance of the specified type
     * @throws IllegalArgumentException if {@code type} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        return (T) resolve(new DefaultObjectQuery(type));
    }

    /**
     * Resolves an instance of the specified generic type using a
     * {@link TypeReference}.
     * <p>
     * This method allows resolving objects of generic types, which cannot be
     * handled by {@link #resolve(Class)} due to type erasure.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Resolve a List of Strings
     * List&lt;String&gt; list = context.resolve(new TypeReference&lt;List&lt;String&gt;&gt;() { });
     * </pre>
     *
     * @param typeReference the type reference describing the generic type to
     *                      resolve
     * @param <T>           the type of the object to resolve
     * @return an instance of the specified generic type
     * @throws IllegalArgumentException if {@code typeReference} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(TypeReference<T> typeReference) {
        return (T) resolve(new DefaultObjectQuery(typeReference.getType()));
    }

    /**
     * Resolves an object for the given {@link ObjectQuery} using this context.
     * <p>
     * This method generates an object based on the provided query and applies
     * post-processing using the configured {@link ObjectProcessor}.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Create a ResolutionContext
     * ResolutionContext context = new ResolutionContext();
     *
     * // Create a custom ObjectQuery for a specific type
     * ObjectQuery query = new DefaultObjectQuery(MyClass.class);
     *
     * // Resolve an object using the query
     * MyClass value = (MyClass) context.resolve(query);
     * </pre>
     *
     * @param query the object query describing the requested object
     * @return the resolved object
     * @throws IllegalArgumentException if {@code query} is {@code null}
     * @throws RuntimeException         if object generation fails
     */
    public Object resolve(ObjectQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("The argument 'query' is null.");
        }

        Object value = generateValue(query);
        processValue(query, value);
        return value;
    }

    private Object generateValue(ObjectQuery query) {
        final Type type = query.getType();
        if (ResolutionContext.class.equals(type)) {
            return this;
        } else if (ObjectGenerator.class.equals(type)) {
            return generator;
        } else if (ObjectProcessor.class.equals(type)) {
            return processor;
        } else {
            try {
                return generator.generate(query, this).unwrapOrElseThrow();
            } catch (UnwrapFailedException exception) {
                throw composeGenerationFailedException(query, exception);
            }
        }
    }

    private RuntimeException composeGenerationFailedException(
        ObjectQuery query,
        Throwable cause
    ) {
        String messageFormat = "Object cannot be created with the given query '%s'."
            + " This can happen if the query represents an interface or abstract class.";
        String message = String.format(messageFormat, query);
        return new RuntimeException(message, cause);
    }

    private void processValue(ObjectQuery query, Object value) {
        processor.process(query, value, this);
    }

    /**
     * Applies the specified {@link Customizer} to customize the object
     * generator and processor used by this context.
     * <p>
     * This method allows you to modify or extend the behavior of object
     * generation and post-processing by providing a custom implementation of
     * {@link Customizer}.
     * </p>
     *
     * <p><b>Example:</b></p>
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
     * // Test using the custom generator
     * &#64;Test
     * &#64;AutoParams
     * void testMethod(ResolutionContext context) {
     *     // Apply the custom StringGenerator
     *     context.applyCustomizer(new StringGenerator());
     *
     *     // Resolve a String value using the customized context
     *     String value = context.resolve();
     *
     *     // Assert that the resolved value is "hello world"
     *     assertEquals("hello world", value);
     * }
     * </pre>
     *
     * @param customizer the customizer to apply
     * @throws IllegalArgumentException if {@code customizer} is {@code null}
     */
    public void applyCustomizer(Customizer customizer) {
        if (customizer == null) {
            throw new IllegalArgumentException("The argument 'customizer' is null.");
        }

        generator = customizer.customize(generator);
        processor = customizer.customize(processor);
    }

    /**
     * Applies the specified {@link ObjectGenerator} to customize the object
     * generator used by this context.
     * <p>
     * This method allows you to change the object generation behavior by
     * providing a custom implementation of {@link ObjectGenerator}.
     * </p>
     *
     * <p><b>Example:</b></p>
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
     * // Test using the custom generator
     * &#64;Test
     * &#64;AutoParams
     * void testMethod(ResolutionContext context) {
     *     // Apply the custom StringGenerator
     *     context.applyCustomizer(new StringGenerator());
     *
     *     // Resolve a String value using the customized context
     *     String value = context.resolve();
     *
     *     // Assert that the resolved value is "hello world"
     *     assertEquals("hello world", value);
     * }
     * </pre>
     *
     * @param generator the object generator to apply
     * @throws IllegalArgumentException if {@code generator} is {@code null}
     */
    public void applyCustomizer(ObjectGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        this.generator = generator.customize(this.generator);
    }

    /**
     * Applies the specified {@link ObjectProcessor} to customize the object
     * processor used by this context.
     * <p>
     * This method allows you to change the post-processing behavior by
     * providing a custom implementation of {@link ObjectProcessor}.
     * </p>
     *
     * <p><b>Example:</b></p>
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
     * // Test using the custom processor
     * &#64;Test
     * &#64;AutoParams
     * void testMethod(ResolutionContext context) {
     *     // Apply the custom LoggingProcessor
     *     context.applyCustomizer(new LoggingProcessor());
     *
     *     // Resolve a value (processing will be logged)
     *     String value = context.resolve();
     * }
     * </pre>
     *
     * @param processor the object processor to apply
     * @throws IllegalArgumentException if {@code processor} is {@code null}
     */
    public void applyCustomizer(ObjectProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        this.processor = processor.customize(this.processor);
    }

    /**
     * Applies {@link Customizer} instances to this context.
     * <p>
     * This method allows you to sequentially apply multiple customizers to modify
     * or extend the behavior of the object generator and processor used by this context.
     * Each customizer is applied in the order provided.
     * </p>
     *
     * <p><b>Example:</b></p>
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
     * // Usage in a test or setup
     * ResolutionContext context = new ResolutionContext();
     * context.customize(new StringGenerator(), new LoggingProcessor());
     * String value = context.resolve(); // Will use the custom generator and processor
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
     *
     *     &#64;Test
     *     &#64;AutoParams
     *     void testMethod(Product product, &#64;Max(5) int rating, ResolutionContext context) {
     *         context.customize(
     *             set(Review::getProduct).to(product),
     *             set(Review::getRating).to(rating)
     *         );
     *         Review review = context.resolve();
     *         assertSame(product, review.getProduct());
     *         assertEquals(rating, review.getRating());
     *     }
     * }
     * </pre>
     *
     * @param customizers customizers to apply
     */
    public void customize(Customizer... customizers) {
        for (Customizer customizer : customizers) {
            applyCustomizer(customizer);
        }
    }

    /**
     * Creates a new {@link ResolutionContext} branched from the current context
     * with additional customizers applied.
     * <p>
     * This method returns a new instance of {@link ResolutionContext} that
     * inherits the current generator and processor, then sequentially applies
     * the given customizers. The original context remains unchanged.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * ResolutionContext baseContext = new ResolutionContext();
     * ResolutionContext customContext = baseContext.branch(new MyCustomizer());
     * // customContext has MyCustomizer applied, baseContext does not
     * </pre>
     *
     * @param customizers customizers to apply to the branched context
     *                    (optional)
     * @return a new {@link ResolutionContext} with the customizers applied
     */
    public ResolutionContext branch(Customizer... customizers) {
        ResolutionContext context = new ResolutionContext(generator, processor);
        context.customize(customizers);
        return context;
    }
}


