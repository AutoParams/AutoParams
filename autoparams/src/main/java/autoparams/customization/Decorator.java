package autoparams.customization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.generator.ObjectGenerator;

/**
 * An abstract base class that facilitates decorating objects of a specific type
 * during generation.
 * <p>
 * The {@link Decorator Decorator&lt;T&gt;} abstract class implements the
 * {@link Customizer} interface to provide a convenient way to modify or enhance
 * objects of a particular type as they are generated. It automatically detects
 * the target type parameter {@code T} through generic type introspection,
 * allowing subclasses to focus solely on the decoration logic.
 * </p>
 *
 * <p>
 * <b>Example: Decorating String objects with uppercase conversion</b>
 * </p>
 * <p>
 * This example shows how to create a decorator that converts all alphabetic
 * characters in generated String objects to uppercase:
 * </p>
 * <pre>
 * public class UppercaseDecorator extends Decorator&lt;String&gt; {
 *
 *     &#64;Override
 *     protected String decorate(String component) {
 *         return component.toUpperCase();
 *     }
 * }
 * </pre>
 * <p>
 * You can use this decorator to ensure all alphabetic characters in string
 * values are in uppercase:
 * </p>
 * <pre>
 * &#64;Test
 * &#64;AutoParams
 * &#64;Customization(UppercaseDecorator.class)
 * void testMethod(String text) {
 *     // All alphabetic characters in text are in uppercase
 * }
 * </pre>
 *
 * @param <T> the type of objects to be decorated
 * @see Customizer
 * @see ObjectGenerator
 */
public abstract class Decorator<T> implements Customizer {

    private final Type componentType;

    /**
     * Constructs a new {@code Decorator} instance.
     * <p>
     * This constructor detects the component type (T) from the generic type
     * declaration of the subclass. This allows decorators to automatically
     * target the correct object type without manual configuration.
     * </p>
     */
    protected Decorator() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType reference = (ParameterizedType) superClass;
        Type[] typeArguments = reference.getActualTypeArguments();
        componentType = typeArguments[0];
    }

    /**
     * Creates a new {@link ObjectGenerator} that decorates objects of the
     * target type.
     * <p>
     * This method intercepts object generation for the specific component type
     * and applies the decoration logic defined in the {@link #decorate(Object)
     * decorate(T)} method. Objects of other types are generated without
     * modification.
     * </p>
     *
     * @param generator the base object generator to be decorated
     * @return a new object generator that applies decoration to objects of the
     *         target type
     * @see #decorate
     */
    @SuppressWarnings("unchecked")
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            if (query.getType().equals(componentType)) {
                return generator
                    .generate(query, context)
                    .process(component -> decorate((T) component));
            } else {
                return generator.generate(query, context);
            }
        };
    }

    /**
     * Decorates or transforms an object of the target type.
     * <p>
     * This method defines the decoration logic to be applied to objects of type
     * {@code T}. Implement this method to specify how objects should be
     * modified or enhanced during the generation process.
     * </p>
     *
     * @param component the original object to be decorated
     * @return the decorated object
     */
    protected abstract T decorate(T component);
}
