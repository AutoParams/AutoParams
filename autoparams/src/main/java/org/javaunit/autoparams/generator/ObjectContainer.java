package org.javaunit.autoparams.generator;

import java.util.function.Supplier;

public final class ObjectContainer {

    public static final ObjectContainer EMPTY = new ObjectContainer(null);

    private final Object value;

    public ObjectContainer(Object value) {
        this.value = value;
    }

    public ObjectContainer yieldIfEmpty(Supplier<ObjectContainer> next) {
        return this == EMPTY ? next.get() : this;
    }

    public Object unwrapOrElseThrow() {
        if (this == EMPTY) {
            throw new UnwrapFailedException();
        } else {
            return value;
        }
    }

}
