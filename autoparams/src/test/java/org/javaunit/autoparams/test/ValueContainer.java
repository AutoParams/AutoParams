package org.javaunit.autoparams.test;

public class ValueContainer<T> {

    private final T value;

    public ValueContainer(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
