package autoparams;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Represents a query for a specific {@link Field} and its type.
 * <p>
 * This class implements {@link ObjectQuery} for field-based object resolution
 * in the AutoParams framework. It encapsulates a {@link Field} and its
 * associated {@link Type}, providing access to both for object generation or
 * customization scenarios.
 * </p>
 *
 * @see ObjectQuery
 */
public final class FieldQuery implements ObjectQuery {

    private final Field field;
    private final Type type;

    /**
     * Constructs a new {@link FieldQuery} with the given field and type.
     *
     * @param field the field to be queried
     * @param type  the type of the field
     */
    public FieldQuery(Field field, Type type) {
        this.field = field;
        this.type = type;
    }

    /**
     * Returns the field associated with this query.
     *
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * Returns the type of the field associated with this query.
     *
     * @return the field type
     */
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toLog(boolean verbose) {
        String typeName = TypeFormatter.format(type, verbose);
        return typeName + " " + field.getName();
    }

    /**
     * Returns a string representation of this field query.
     *
     * @return a string representation of the field query
     */
    @Override
    public String toString() {
        return "Field " + field;
    }
}
