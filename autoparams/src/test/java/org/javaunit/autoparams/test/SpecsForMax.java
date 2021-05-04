package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

public class SpecsForMax {

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_int(@Max(100) int value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_with_big_value_for_int(@Min(0) @Max(0x80000000L) int value) {
        assertThat(value).isGreaterThanOrEqualTo(0);
    }

}
