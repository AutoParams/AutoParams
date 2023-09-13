package autoparams.generator;

import java.lang.reflect.Type;

public final class TypeQuery implements ObjectQuery {

    private final Type type;

    public TypeQuery(Type type) {
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
