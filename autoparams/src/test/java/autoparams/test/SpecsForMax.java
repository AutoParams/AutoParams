package autoparams.test;

import autoparams.AutoSource;
import autoparams.Repeat;
import javax.validation.constraints.Max;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForMax {

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_double(@Max(100) double value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(100)
    void sut_accepts_max_constraint_for_float(@Max(100) float value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }
}
