package test.autoparams;

import java.time.ZoneId;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForZoneId {

    @ParameterizedTest
    @AutoSource
    void sut_creates_ZoneId_value(ZoneId zoneId) {
        assertNotNull(zoneId);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_anonymous_ZoneId_value(ZoneId value1, ZoneId value2, ZoneId value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
