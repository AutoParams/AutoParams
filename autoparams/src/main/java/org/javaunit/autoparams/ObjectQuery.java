package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class ObjectQuery {

    private final Class<?> type;

    public ObjectQuery(Class<?> type) {
        this.type = type;
    }

    public static ObjectQuery create(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType instanceof ParameterizedType) {
            return new GenericObjectQuery(parameter.getType(),
                (ParameterizedType) parameterizedType);
        } else {
            return new ObjectQuery(parameter.getType());
        }
    }

    public Class<?> getType() {
        return type;
    }

}
