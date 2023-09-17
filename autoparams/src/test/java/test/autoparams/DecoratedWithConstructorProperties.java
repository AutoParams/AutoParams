package test.autoparams;

import java.beans.ConstructorProperties;

public class DecoratedWithConstructorProperties {

    private final String value1;
    private final String value2;

    public DecoratedWithConstructorProperties() {
        this(null, null);
    }

    @ConstructorProperties({"value1"})
    public DecoratedWithConstructorProperties(String value1) {
        this(value1, null);
    }

    public DecoratedWithConstructorProperties(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }
}
