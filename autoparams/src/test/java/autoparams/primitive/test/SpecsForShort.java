package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import autoparams.Repeat;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForShort {

    @AutoParameterizedTest
    void sut_creates_arbitrary_short_values(
        short value1,
        short value2,
        short value3,
        short value4,
        short value5
    ) {
        HashSet<Short> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Short_values(
        Short value1,
        Short value2,
        Short value3,
        Short value4,
        Short value5
    ) {
        HashSet<Short> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_short_value(short arg) {
        assertThat(arg).isPositive();
    }
}
