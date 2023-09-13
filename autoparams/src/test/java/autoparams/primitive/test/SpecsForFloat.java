package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SpecsForFloat {

    @AutoParameterizedTest
    void sut_creates_arbitrary_float_values(
        float value1,
        float value2,
        float value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Float_values(
        Float value1,
        Float value2,
        Float value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
