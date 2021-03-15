package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ObjectQuery {

    private final Class<?> type;
    private final List<? extends Annotation> annotations;

    public ObjectQuery(Class<?> type) {
        this(type, Collections.emptyList());
    }
    public ObjectQuery(Class<?> type, List<Annotation> annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    public static ObjectQuery create(Parameter parameter) {
        List<Annotation> annotations = Arrays.asList(parameter.getAnnotations());
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType instanceof ParameterizedType) {
            return new GenericObjectQuery(parameter.getType(), (ParameterizedType) parameterizedType, annotations);
        } else {
            return new ObjectQuery(parameter.getType(), annotations);
        }
    }

    public Class<?> getType() {
        return type;
    }

    // 타입변환으로 인한 빌드 워닝 억제
    @SuppressWarnings("unchecked")
    public <T extends Annotation> Optional<T> findAnnotation(Class<T> type) {
        return (Optional<T>) annotations.stream().filter(a -> a.annotationType().equals(type)).findFirst();
    }
}
