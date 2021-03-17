package org.javaunit.autoparams;

import java.beans.ConstructorProperties;

public class DecoratedWithConstructorProperties {

    private final String value;

    public DecoratedWithConstructorProperties() {
        this(null);
    }

    @ConstructorProperties({"value"})
    public DecoratedWithConstructorProperties(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
