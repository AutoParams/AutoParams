package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.UUID;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForSimpleTypes {

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_string_values_for_object_parameter(
        Object value1,
        Object value2,
        Object value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);

        assertThat(value1).isInstanceOf(String.class);
        assertThat(value2).isInstanceOf(String.class);
        assertThat(value3).isInstanceOf(String.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_string_values(String value1, String value2, String value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_uuid_values(UUID value1, UUID value2, UUID value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_big_integer_values(
        BigInteger value1,
        BigInteger value2,
        BigInteger value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_big_decimal_values(
        BigDecimal value1,
        BigDecimal value2,
        BigDecimal value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_duration_values(
        Duration value1,
        Duration value2,
        Duration value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_local_date_values(
        LocalDate value1,
        LocalDate value2,
        LocalDate value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_local_time_values(
        LocalTime value1,
        LocalTime value2,
        LocalTime value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_local_date_time_values(
        LocalDateTime value1,
        LocalDateTime value2,
        LocalDateTime value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_period_values(
        Period value1,
        Period value2,
        Period value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_supports_normalizing_period_values(
        Period value1,
        Period value2,
        Period value3
    ) {
        assertDoesNotThrow(value1::normalized);
        assertDoesNotThrow(value2::normalized);
        assertDoesNotThrow(value3::normalized);
    }
}
