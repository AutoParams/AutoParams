package test.autoparams;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

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
    void sut_creates_anonymous_ZoneId_value(
        ZoneId value1,
        ZoneId value2,
        ZoneId value3,
        ZoneId value4,
        ZoneId value5
    ) {
        Set<ZoneId> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertNotEquals(1, set.size());
    }
}
