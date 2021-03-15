package org.javaunit.autoparams;

import java.util.UUID;

public class ComplexObject {

    private final int value1;
    private final String value2;
    private final UUID value3;

    public ComplexObject(int value1, String value2, UUID value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public int getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public UUID getValue3() {
        return value3;
    }

}
