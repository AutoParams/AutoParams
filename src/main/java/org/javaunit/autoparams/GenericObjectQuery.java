package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;

final class GenericObjectQuery extends ObjectQuery {
    
    private final ParameterizedType parameterizedType;

    public GenericObjectQuery(Class<?> type, ParameterizedType parameterizedType) {
        super(type);
        this.parameterizedType = parameterizedType;
    }

    public ParameterizedType getParameterizedType() {
        return parameterizedType;
    }

}
