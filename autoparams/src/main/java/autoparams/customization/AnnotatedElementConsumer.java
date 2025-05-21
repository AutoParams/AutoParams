package autoparams.customization;

import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@link AnnotatedElement}
 * argument and returns no result.
 * <p>
 * This is a functional interface whose functional method is
 * {@link Consumer#accept}. It is typically used in scenarios where an action
 * needs to be performed on an annotated element, such as processing annotations
 * or modifying behavior based on them.
 * </p>
 *
 * @see AnnotatedElement
 * @see Consumer
 */
@FunctionalInterface
public interface AnnotatedElementConsumer extends Consumer<AnnotatedElement> {
}
