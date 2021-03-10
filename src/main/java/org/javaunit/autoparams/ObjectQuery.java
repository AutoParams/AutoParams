package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

final class ObjectQuery {

    private final Class<?> type;
    private final Type parameterizedType;

    public ObjectQuery(Class<?> type) {
        this(type, type);
    }

    public ObjectQuery(Class<?> type, Type parameterizedType) {
        this.type = type;
        this.parameterizedType = parameterizedType;
    }

    public static ObjectQuery create(Parameter parameter) {
        return new ObjectQuery(parameter.getType(), parameter.getParameterizedType());
    }

    public Class<?> getType() {
        return type;
    }

    public Type getParameterizedType() {
        return parameterizedType;
    }

}
