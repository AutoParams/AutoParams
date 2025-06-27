package autoparams;

import java.lang.reflect.Type;

/**
 * Represents a query for an object of a specific {@link Type}.
 * <p>
 * Implementations of this interface provide type information used for object
 * generation in the AutoParams framework.
 * </p>
 *
 * @see DefaultObjectQuery
 * @see ParameterQuery
 * @see FieldQuery
 */
@FunctionalInterface
public interface ObjectQuery {

    /**
     * Returns the {@link Type} that this query represents.
     *
     * @return the type information for object generation
     */
    Type getType();

    default String toLog(boolean verbose) {
        return TypeFormatter.format(getType(), verbose);
    }
}
