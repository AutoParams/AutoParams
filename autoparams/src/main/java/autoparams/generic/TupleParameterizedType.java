package autoparams.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class TupleParameterizedType implements ParameterizedType {

    private final Type[] actualTypeArguments;
    private final Type rawType;
    private final Type ownerType;

    public TupleParameterizedType(
        Type[] actualTypeArguments,
        Type rawType,
        Type ownerType
    ) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}
