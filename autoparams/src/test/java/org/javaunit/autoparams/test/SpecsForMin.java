package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

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

}
