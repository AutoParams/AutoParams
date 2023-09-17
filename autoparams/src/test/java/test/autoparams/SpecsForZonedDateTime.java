package test.autoparams;

import autoparams.AutoSource;
import autoparams.customization.Fix;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForZonedDateTime {

    @ParameterizedTest
    @AutoSource
    void sut_creates_ZonedDateTime_value(ZonedDateTime zonedDateTime) {
        assertNotNull(zonedDateTime);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_anonymous_ZonedDateTime_value(
        ZonedDateTime value1, ZonedDateTime value2, ZonedDateTime value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fixes_value_by_ZoneId(
        @Fix ZoneId fixedZoneId, ZonedDateTime zonedDateTime
    ) {
        assertEquals(fixedZoneId, zonedDateTime.getZone());
    }
}
