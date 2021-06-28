package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.constraints.Min;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

public class SpecsForMin {

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int(@Min(100) int value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_double(@Min(100) double value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_long(@Min(100) long value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int_when_over_upper_bound(@Min(Long.MAX_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int_when_over_lower_bound(@Min(Long.MIN_VALUE) int value) {
        assertThat(value).isGreaterThanOrEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_with_max_value_for_long(@Min(Long.MAX_VALUE) long value) {
        assertEquals(Long.MAX_VALUE, value);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_short(@Min(100) short value) {
        assertThat(value).isGreaterThanOrEqualTo((short) 100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_short_when_over_upper_bound(@Min(Long.MAX_VALUE) short value) {
        assertThat(value).isEqualTo(Short.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_short_when_over_lower_bound(@Min(Long.MIN_VALUE) short value) {
        assertThat(value).isGreaterThanOrEqualTo(Short.MIN_VALUE);
    }

}
