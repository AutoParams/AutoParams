package autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

/**
 * Base class for implementing {@link ObjectGenerator} with type inference
 * support.
 * <p>
 * This abstract class provides a template for object generators that generate
 * objects of a specific type. Subclasses should implement the
 * {@link #generateObject(ObjectQuery, ResolutionContext)} method to provide the
 * actual object generation logic.
 * </p>
 * <p>
 * The generate method checks if the requested type matches the inferred type
 * and delegates to the subclass's implementation if so. Otherwise, it returns
 * {@link ObjectContainer#EMPTY}.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <p>
 * The following example shows a custom generator that produces String objects
 * only.
 * </p>
 * <pre>
 * // Generates String objects only
 * public class StringGenerator extends ObjectGeneratorBase&lt;String&gt; {
 *
 *     &#64;Override
 *     protected String generateObject(ObjectQuery query, ResolutionContext context) {
 *         return "hello world";
 *     }
 * }
 * </pre>
 *
 * @param <T> the type of object to generate
 * @see ObjectGenerator
 * @see ObjectQuery
 * @see ObjectContainer
 * @see ResolutionContext
 */
public abstract class ObjectGeneratorBase<T> implements ObjectGenerator {

    private final Type type = inferType(getClass());

    private static Type inferType(Class<?> generatorType) {
        try {
            Method method = generatorType.getDeclaredMethod(
                "generateObject",
                ObjectQuery.class,
                ResolutionContext.class
            );
            return method.getGenericReturnType();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Constructs an instance of
     * {@link ObjectGeneratorBase ObjectGeneratorBase&lt;T&gt;}.
     */
    protected ObjectGeneratorBase() {
    }

    /**
     * Generates an {@link ObjectContainer} for the given {@link ObjectQuery}
     * and {@link ResolutionContext} if the requested type matches the inferred
     * type.
     * <p>
     * If the type requested by the {@link ObjectQuery} matches the type
     * parameter of this generator, this method delegates to the subclass's
     * {@link #generateObject(ObjectQuery, ResolutionContext)} implementation
     * and wraps the result in an {@link ObjectContainer}. If the types do not
     * match, {@link ObjectContainer#EMPTY} is returned.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param context the resolution context for object generation
     * @return an {@link ObjectContainer} with the generated object, or
     *         {@link ObjectContainer#EMPTY} if the type does not match
     * @see ObjectContainer
     * @see ObjectQuery
     * @see ResolutionContext
     */
    @Override
    public final ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (matches(query.getType())) {
            return new ObjectContainer(generateObject(query, context));
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    private boolean matches(Type type) {
        return type instanceof Class && matches((Class<?>) type);
    }

    private boolean matches(Class<?> type) {
        return Modifier.isAbstract(type.getModifiers())
            && this.type instanceof Class
            ? type.isAssignableFrom((Class<?>) this.type)
            : type.equals(this.type);
    }

    /**
     * Generates an object of type {@code T} for the given {@link ObjectQuery}
     * and {@link ResolutionContext}.
     * <p>
     * Subclasses must implement this method to provide the logic for generating
     * an object of the target type. This method is called by the
     * {@link #generate(ObjectQuery, ResolutionContext)} method when the
     * requested type matches the generator's type parameter.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <p>
     * This example shows a generator that always returns the string
     * {@code "hello world"}.
     * </p>
     * <pre>
     * // Always returns "hello world"
     * public class StringGenerator extends ObjectGeneratorBase&lt;String&gt; {
     *
     *     &#64;Override
     *     protected String generateObject(ObjectQuery query, ResolutionContext context) {
     *         return "hello world";
     *     }
     * }
     * </pre>
     *
     * @param query   the object query describing the requested object
     * @param context the resolution context for object generation
     * @return the generated object of type {@code T}
     * @see ObjectQuery
     * @see ResolutionContext
     */
    protected abstract T generateObject(
        ObjectQuery query,
        ResolutionContext context
    );
}
