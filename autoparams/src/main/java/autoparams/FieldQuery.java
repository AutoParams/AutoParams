package autoparams;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public final class FieldQuery implements ObjectQuery {

    private final Field field;
    private final Type type;

    public FieldQuery(Field field, Type type) {
        this.field = field;
        this.type = type;
    }

    public Field getField() {
        return field;
    }

    @Override
    public Type getType() {
        return type;
    }
}
