package org.javaunit.autoparams;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class GenericType {

    private final TypeVariable<?> typeVariable;
    private final Type typeValue;

    public GenericType(TypeVariable<?> typeVariable, Type typeValue) {
        this.typeVariable = typeVariable;
        this.typeValue = typeValue;
    }

    public TypeVariable<?> getTypeVariable() {
        return typeVariable;
    }

    public Type getTypeValue() {
        return typeValue;
    }

}
