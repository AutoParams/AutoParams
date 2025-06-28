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

    /**
     * Returns a log-friendly string representation of this object query.
     * <p>
     * This method provides a concise representation suitable for logging
     * purposes, with the format controlled by the verbose parameter.
     * </p>
     *
     * @param verbose if {@code true}, returns detailed format with full
     *                package names; if {@code false}, returns simple format
     *                with short type names
     * @return a string representation of the query for logging
     */
    default String toLog(boolean verbose) {
        return TypeFormatter.format(getType(), verbose);
    }
}
