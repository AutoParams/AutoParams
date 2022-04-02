package org.javaunit.autoparams.test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForInstant {

    @ParameterizedTest
    @AutoSource
    void sut_creates_Instant_value(Instant instant) {
        assertNotNull(instant);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_randomized_instant_value(Instant value1, Instant value2, Instant value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
