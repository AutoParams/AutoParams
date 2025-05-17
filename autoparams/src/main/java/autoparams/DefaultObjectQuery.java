package autoparams;

import java.lang.reflect.Type;

/**
 * Default implementation of the {@link ObjectQuery} interface.
 * <p>
 * This class represents a query for an object of a specific {@link Type}.
 * It is typically used to encapsulate type information when requesting
 * object generation in the AutoParams framework.
 * </p>
 */
public final class DefaultObjectQuery implements ObjectQuery {

    private final Type type;

    /**
     * Constructs a new {@link DefaultObjectQuery} with the specified type.
     *
     * @param type the {@link Type} representing the object to be queried
     */
    public DefaultObjectQuery(Type type) {
        this.type = type;
    }

    /**
     * Returns the {@link Type} represented by this query.
     *
     * @return the type of the object to be generated
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * Returns a string representation of this query.
     * <p>
     * The returned string is the string representation of the underlying
     * {@link Type}.
     * </p>
     *
     * @return a string representation of the type associated with this query
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
