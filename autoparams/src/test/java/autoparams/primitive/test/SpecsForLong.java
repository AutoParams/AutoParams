package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import autoparams.Repeat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SpecsForLong {

    @AutoParameterizedTest
    void sut_creates_arbitrary_long_values(
        long value1,
        long value2,
        long value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Long_values(
        Long value1,
        Long value2,
        Long value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_long_value(long arg) {
        assertThat(arg).isPositive();
    }
}
