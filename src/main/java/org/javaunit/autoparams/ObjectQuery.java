package org.javaunit.autoparams;

import java.lang.reflect.Parameter;

final class ObjectQuery {

    private final Class<?> type;

    public ObjectQuery(Class<?> type) {
        this.type = type;
    }

    public static ObjectQuery create(Parameter parameter) {
        return new ObjectQuery(parameter.getType());
    }

    public Class<?> getType() {
        return type;
    }

}
