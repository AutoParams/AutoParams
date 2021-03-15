package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

final class GenericObjectQuery extends ObjectQuery {

    private final ParameterizedType parameterizedType;

    public GenericObjectQuery(Class<?> type, ParameterizedType parameterizedType) {
        this(type, parameterizedType, Collections.emptyList());
    }

    public GenericObjectQuery(Class<?> type, ParameterizedType parameterizedType, List<Annotation> annotations) {
        super(type, annotations);
        this.parameterizedType = parameterizedType;
    }

    public ParameterizedType getParameterizedType() {
        return parameterizedType;
    }

}
