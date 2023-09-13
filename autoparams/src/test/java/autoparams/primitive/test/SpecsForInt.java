package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import autoparams.Repeat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SpecsForInt {

    @AutoParameterizedTest
    void sut_creates_arbitrary_int_values(
        int value1,
        int value2,
        int value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Integer_values(
        Integer value1,
        Integer value2,
        Integer value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_int_value(int arg) {
        assertThat(arg).isPositive();
    }
}
