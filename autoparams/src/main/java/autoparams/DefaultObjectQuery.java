package autoparams;

import java.lang.reflect.Type;

public final class DefaultObjectQuery implements ObjectQuery {

    private final Type type;

    public DefaultObjectQuery(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
