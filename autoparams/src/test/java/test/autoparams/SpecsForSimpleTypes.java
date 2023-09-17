package test.autoparams;

import autoparams.Repeat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SpecsForSimpleTypes {

    @AutoParameterizedTest
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

    @AutoParameterizedTest
    void sut_creates_arbitrary_string_values(String value1, String value2, String value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_uuid_values(UUID value1, UUID value2, UUID value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_big_integer_values(
        BigInteger value1,
        BigInteger value2,
        BigInteger value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_big_decimal_values(
        BigDecimal value1,
        BigDecimal value2,
        BigDecimal value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_number_values(
        Number value1,
        Number value2,
        Number value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_duration_values(
        Duration value1,
        Duration value2,
        Duration value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_temporal_amount_values(
        TemporalAmount value1,
        TemporalAmount value2,
        TemporalAmount value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_local_date_values(
        LocalDate value1,
        LocalDate value2,
        LocalDate value3,
        LocalDate value4,
        LocalDate value5
    ) {
        final HashSet<LocalDate> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_local_time_values(
        LocalTime value1,
        LocalTime value2,
        LocalTime value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_local_date_time_values(
        LocalDateTime value1,
        LocalDateTime value2,
        LocalDateTime value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_period_values(
        Period value1,
        Period value2,
        Period value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_normalized_period_value(Period value) {
        assertThat(value).isEqualTo(value.normalized());
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_chrono_period_values(
        ChronoPeriod value1,
        ChronoPeriod value2,
        ChronoPeriod value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_period_value(Period value) {
        assertFalse(value.isNegative());
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_temporal_values(
        Temporal value1,
        Temporal value2,
        Temporal value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }
}
