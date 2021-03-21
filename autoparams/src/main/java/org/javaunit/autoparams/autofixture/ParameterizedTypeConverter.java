package org.javaunit.autoparams.autofixture;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeConverter {
    public static ParameterizedType convert(Type type) {
        if(type instanceof ParameterizedType) {
            return (ParameterizedType)type;
        }
        return null;
    }
}
