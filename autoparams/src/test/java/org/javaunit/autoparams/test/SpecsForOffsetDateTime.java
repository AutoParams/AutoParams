package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Fix;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForOffsetDateTime {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_OffsetDateTime_value(OffsetDateTime arg) {
        assertNotNull(arg);
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

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_fixed_OffsetDateTime_value(@Fix ZoneId zoneId, @Fix Instant instant,
                                                OffsetDateTime arg) {
        assertEquals(instant.atZone(zoneId).toOffsetDateTime(), arg);
    }

}
