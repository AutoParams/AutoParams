package org.javaunit.autoparams.test;

import java.util.List;
import java.util.UUID;

public class RecursiveObject {

    private final int value1;
    private final String value2;
    private final UUID value3;
    private final List<RecursiveObject> value4;

    public RecursiveObject(int value1, String value2, UUID value3, List<RecursiveObject> value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
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

    public List<RecursiveObject> getValue4() {
        return value4;
    }

}
