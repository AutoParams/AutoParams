package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @ParameterizedTest
    @AutoSource
    void sut_includes_maximum_value(@Min(1) @Max(1) int value) {
        assertEquals(1, value);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_double(@Max(100) double value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_long(@Max(100) long value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_int_when_over_lower_bound(@Max(Long.MIN_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_int_when_over_upper_bound(@Max(Long.MAX_VALUE) int value) {
        assertThat(value).isLessThanOrEqualTo(Integer.MAX_VALUE);
    }
}
