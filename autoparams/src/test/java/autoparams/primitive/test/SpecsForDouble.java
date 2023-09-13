package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SpecsForDouble {

    @AutoParameterizedTest
    void sut_creates_arbitrary_double_values(
        double value1,
        double value2,
        double value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Double_values(
        Double value1,
        Double value2,
        Double value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
