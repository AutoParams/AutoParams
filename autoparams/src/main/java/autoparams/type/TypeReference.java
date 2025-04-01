package autoparams.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unused")
public abstract class TypeReference<T> {

    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType reference = (ParameterizedType) superClass;
        Type[] typeArguments = reference.getActualTypeArguments();
        type = typeArguments[0];
    }

    public Type getType() {
        return type;
    }
}
