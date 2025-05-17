package autoparams.generator;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Immutable container class that wraps an object to distinguish between an
 * empty container and an actual value.
 * <p>
 * Use the {@link #EMPTY} instance to represent the absence of a value. Provides
 * utility methods for value handling and exception management.
 * </p>
 */
public final class ObjectContainer {

    /**
     * A singleton instance representing an empty container.
     * <p>
     * This instance should be used to represent the absence of a value.
     * </p>
     */
    public static final ObjectContainer EMPTY = new ObjectContainer(null);

    private final Object value;

    /**
     * Creates a new ObjectContainer instance wrapping the given value. Even if
     * the value is null, a distinct container is created, different from
     * {@link #EMPTY}.
     *
     * @param value the value to be stored in the container (nullable)
     */
    public ObjectContainer(Object value) {
        this.value = value;
    }

    /**
     * Returns the result of the given supplier if this container is
     * {@link #EMPTY}, otherwise returns this container itself.
     *
     * @param next a supplier that provides an alternative
     *             {@link ObjectContainer} if this is empty
     * @return this container if not empty, or the result of {@code next.get()}
     *         if empty
     */
    public ObjectContainer yieldIfEmpty(Supplier<ObjectContainer> next) {
        return this == EMPTY ? next.get() : this;
    }

    /**
     * Returns the contained value if present, otherwise throws an
     * {@link UnwrapFailedException}. Use this method when you expect the
     * container to hold a value and want to fail fast if it is empty.
     *
     * @return the contained value
     * @throws UnwrapFailedException if the container is {@link #EMPTY}
     */
    public Object unwrapOrElseThrow() {
        if (this == EMPTY) {
            throw new UnwrapFailedException();
        } else {
            return value;
        }
    }

    /**
     * Applies the given processor function to the contained value if this
     * container is not {@link #EMPTY}, and returns a new
     * {@link ObjectContainer} wrapping the result. If this container is
     * {@link #EMPTY}, returns itself.
     *
     * @param processor a function to process the contained value
     * @return a new {@link ObjectContainer} with the processed value, or
     *         {@link #EMPTY} if this is empty
     */
    public ObjectContainer process(Function<Object, Object> processor) {
        return this == EMPTY ? this : new ObjectContainer(processor.apply(value));
    }
}
