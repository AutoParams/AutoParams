package autoparams.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectGenerator;

/**
 * Base class for implementing type-specific {@link ObjectProcessor}s.
 * <p>
 * This abstract class provides a convenient foundation for creating object
 * processors that only process objects of a specific type. It automatically
 * checks the type of the generated object and delegates processing to the
 * {@link #processObject} method if the type matches.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * // This processor only processes objects of type MyClass
 * public class MyClassProcessor extends ObjectProcessorBase&lt;MyClass&gt; {
 *
 *     &#64;Override
 *     protected void processObject(ObjectQuery query, MyClass value, ResolutionContext context) {
 *         // Custom processing logic for MyClass
 *     }
 * }
 * </pre>
 * <p>
 * This example shows how to implement a processor that only processes a
 * specific type.
 * </p>
 *
 * @param <T> the type of object to process
 * @see ObjectProcessor
 * @see ResolutionContext
 */
public abstract class ObjectProcessorBase<T> implements ObjectProcessor {

    private final Type type = inferType(getClass());

    private Type inferType(Class<?> processorType) {
        return getBaseType(processorType).getActualTypeArguments()[0];
    }

    private static ParameterizedType getBaseType(Class<?> processorType) {
        return (ParameterizedType) processorType.getGenericSuperclass();
    }

    /**
     * Processes the given object if its type matches the processor's type
     * parameter.
     * <p>
     * This method checks whether the type of the object described by the
     * provided {@link ObjectQuery} matches the type parameter {@code T} of this
     * processor. If the types match, it delegates processing to the
     * {@link #processObject} method, passing the query, the cast value, and the
     * resolution context.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param value   the generated object to process
     * @param context the resolution context for further object resolution
     * @see #processObject(ObjectQuery, Object, ResolutionContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void process(
        ObjectQuery query,
        Object value,
        ResolutionContext context
    ) {
        if (matches(query.getType())) {
            processObject(query, (T) value, context);
        }
    }

    private boolean matches(Type type) {
        return type instanceof Class && matches((Class<?>) type);
    }

    private boolean matches(Class<?> type) {
        return this.type instanceof Class<?>
            && ((Class<?>) this.type).isAssignableFrom(type);
    }

    /**
     * Processes the given object of type {@code T} after it has been generated.
     * <p>
     * Subclasses implement this method to perform type-specific processing or
     * customization on objects of type {@code T}. This method is called only
     * when the type of the object matches the processor's type parameter.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param value   the generated object of type {@code T} to process
     * @param context the resolution context for further object resolution
     * @see ObjectProcessor#process(ObjectQuery, Object, ResolutionContext)
     */
    protected abstract void processObject(
        ObjectQuery query,
        T value,
        ResolutionContext context
    );

    /**
     * Customizes this {@link ObjectProcessor} by composing it with another processor.
     * <p>
     * The returned processor will first apply the given {@code processor}, then
     * this processor. This allows for chaining multiple processors performing
     * additional processing steps in sequence.
     * </p>
     *
     * @param processor the processor to compose with this processor
     * @return a new {@link ObjectProcessor} that applies both processors in
     *         sequence
     * @see ObjectProcessor#customize(ObjectProcessor)
     */
    @Override
    public final ObjectProcessor customize(ObjectProcessor processor) {
        return ObjectProcessor.super.customize(processor);
    }

    /**
     * Customizes the given {@link ObjectGenerator}.
     * <p>returns the given generator without modification.</p>
     *
     * @param generator the object generator to customize
     * @return the customized object generator
     * @throws IllegalArgumentException if the argument {@code generator} is
     *                                  null
     */
    @Override
    public final ObjectGenerator customize(ObjectGenerator generator) {
        return ObjectProcessor.super.customize(generator);
    }
}
