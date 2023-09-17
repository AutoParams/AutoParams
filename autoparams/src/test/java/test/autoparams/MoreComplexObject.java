package test.autoparams;

import java.util.UUID;

public class MoreComplexObject {

    private final int value1;
    private final String value2;
    private final UUID value3;
    private final ComplexObject value4;

    public MoreComplexObject(int value1, String value2, UUID value3, ComplexObject value4) {
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

    public ComplexObject getValue4() {
        return value4;
    }

}
