package autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import autoparams.AutoSource;
import java.time.OffsetDateTime;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForOffsetDateTime {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_OffsetDateTime_value(OffsetDateTime arg) {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_anonymous_OffsetDateTime_value(
        OffsetDateTime arg1, OffsetDateTime arg2, OffsetDateTime arg3
    ) {
        assertThat(arg1).isNotEqualTo(arg2);
        assertThat(arg2).isNotEqualTo(arg3);
        assertThat(arg3).isNotEqualTo(arg1);
    }

}
