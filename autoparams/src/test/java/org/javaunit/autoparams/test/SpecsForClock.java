package org.javaunit.autoparams.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Clock;
import java.time.ZoneId;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Fix;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForClock {

    @ParameterizedTest
    @AutoSource
    void sut_create_clock(Clock clock) {

        assertNotNull(clock);
    }

    @ParameterizedTest
    @AutoSource
    void sut_create_clock_with_fixed_zoneId(@Fix ZoneId zoneId, Clock clock) {

        assertEquals(zoneId, clock.getZone());
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_randomized_clock(Clock value1, Clock value2, Clock value3) {

        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
