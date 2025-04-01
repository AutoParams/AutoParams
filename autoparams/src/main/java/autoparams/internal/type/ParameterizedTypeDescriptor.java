package autoparams.internal.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class ParameterizedTypeDescriptor implements ParameterizedType {

    private final Type[] actualTypeArguments;
    private final Type rawType;
    private final Type ownerType;

    public ParameterizedTypeDescriptor(
        Type[] actualTypeArguments,
        Type rawType,
        Type ownerType
    ) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}
