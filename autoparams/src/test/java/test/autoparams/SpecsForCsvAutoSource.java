package test.autoparams;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

import autoparams.AutoSource;
import autoparams.CsvAutoSource;
import autoparams.customization.Freeze;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("unused")
class SpecsForCsvAutoSource {

    @ParameterizedTest
    @CsvAutoSource({ "1, foo" })
    void sut_correctly_fills_arguments(int value1, String value2) {
        assertEquals(1, value1);
        assertEquals("foo", value2);
    }

    @ParameterizedTest
    @CsvAutoSource({ "1, foo" })
    void sut_correctly_fills_forepart_arguments(
        int value1,
        String value2,
        UUID value3
    ) {
        assertEquals(1, value1);
        assertEquals("foo", value2);
    }

    @ParameterizedTest
    @CsvAutoSource({ "1, foo" })
    void sut_arbitrarily_generates_remaining_arguments(
        int value1,
        String value2,
        UUID value3,
        UUID value4
    ) {
        assertNotNull(value3);
        assertNotNull(value4);
        assertNotEquals(value3, value4);
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_int_value(@Freeze int value, IntBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_Integer_value(
        @Freeze Integer value,
        GenericBag<Integer> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_long_value(@Freeze long value, LongBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_Long_value(
        @Freeze Long value,
        GenericBag<Long> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_short_value(@Freeze short value, ShortBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_Short_value(
        @Freeze Short value,
        GenericBag<Short> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_byte_value(@Freeze byte value, ByteBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16", "0x01" })
    void sut_reuses_frozen_Byte_value(
        @Freeze Byte value,
        GenericBag<Byte> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "12.34", "Infinity" })
    void sut_reuses_frozen_float_value(@Freeze float value, FloatBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "12.34", "Infinity" })
    void sut_reuses_frozen_Float_value(
        @Freeze Float value,
        GenericBag<Float> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "12.34", "Infinity" })
    void sut_reuses_frozen_double_value(@Freeze double value, DoubleBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "12.34", "Infinity" })
    void sut_reuses_frozen_Double_value(
        @Freeze Double value,
        GenericBag<Double> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "true", "false" })
    void sut_reuses_frozen_boolean_value(
        @Freeze boolean value,
        BooleanBag bag
    ) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "true", "false" })
    void sut_reuses_frozen_Boolean_value(
        @Freeze Boolean value,
        GenericBag<Boolean> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "a", "b", "c" })
    void sut_reuses_frozen_char_value(@Freeze char value, CharBag bag) {
        assertEquals(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "a", "b", "c" })
    void sut_reuses_frozen_Character_value(
        @Freeze Character value,
        GenericBag<Character> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "VALUE1", "VALUE2" })
    void sut_reuses_frozen_Enum_value(
        @Freeze EnumType value,
        GenericBag<EnumType> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "PT1S", "PT2S" })
    void sut_reuses_frozen_Duration_value(
        @Freeze Duration value,
        GenericBag<Duration> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07-01T00:00:00Z", "2021-07-02T00:00:00Z" })
    void sut_reuses_frozen_Instant_value(
        @Freeze Instant value,
        GenericBag<Instant> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07-01T00:00:00", "2021-07-02T00:00:00" })
    void sut_reuses_frozen_LocalDateTime_value(
        @Freeze LocalDateTime value,
        GenericBag<LocalDateTime> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07-01", "2021-07-02" })
    void sut_reuses_frozen_LocalDate_value(
        @Freeze LocalDate value,
        GenericBag<LocalDate> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "00:00:00", "12:34:56" })
    void sut_reuses_frozen_LocalTime_value(
        @Freeze LocalTime value,
        GenericBag<LocalTime> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "--01-01", "--02-02" })
    void sut_reuses_frozen_MonthDay_value(
        @Freeze MonthDay value,
        GenericBag<MonthDay> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07-01T00:00:00Z", "2021-07-02T00:00:00Z" })
    void sut_reuses_frozen_OffSetDateTime_value(
        @Freeze OffsetDateTime value,
        GenericBag<OffsetDateTime> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "00:00:00Z", "12:34:56Z" })
    void sut_reuses_frozen_OffsetTime_value(
        @Freeze OffsetTime value,
        GenericBag<OffsetTime> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "P1D", "P2D" })
    void sut_reuses_frozen_Period_value(
        @Freeze Period value,
        GenericBag<Period> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021", "2022" })
    void sut_reuses_frozen_Year_value(
        @Freeze Year value,
        GenericBag<Year> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07", "2021-08" })
    void sut_reuses_frozen_YearMonth_value(
        @Freeze YearMonth value,
        GenericBag<YearMonth> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "2021-07-01T00:00:00Z", "2021-07-02T00:00:00Z" })
    void sut_reuses_frozen_ZonedDateTime_value(
        @Freeze ZonedDateTime value,
        GenericBag<ZonedDateTime> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "UTC", "Asia/Seoul" })
    void sut_reuses_frozen_ZoneId_value(
        @Freeze ZoneId value,
        GenericBag<ZoneId> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "Z", "+09:00" })
    void sut_reuses_frozen_ZoneOffset_value(
        @Freeze ZoneOffset value,
        GenericBag<ZoneOffset> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({
        "00000000-0000-0000-0000-000000000000",
        "00000000-0000-0000-0000-000000000001",
        "00000000-0000-0000-0000-000000000002"
    })
    void sut_reuses_frozen_UUID_value(
        @Freeze UUID value,
        GenericBag<UUID> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "ko", "en" })
    void sut_reuses_frozen_Locale_value(
        @Freeze Locale value,
        GenericBag<Locale> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "KRW", "USD" })
    void sut_reuses_frozen_Currency_value(
        @Freeze Currency value,
        GenericBag<Currency> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "http://example.com", "http://example.org" })
    void sut_reuses_frozen_URI_value(@Freeze URI value, GenericBag<URI> bag) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "http://example.com", "http://example.org" })
    void sut_reuses_frozen_URL_value(@Freeze URL value, GenericBag<URL> bag) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "src/test/resources/test.csv" })
    void sut_reuses_frozen_Path_value(
        @Freeze Path value,
        GenericBag<Path> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "UTF-8", "ISO-8859-1" })
    void sut_reuses_frozen_Charset_value(
        @Freeze Charset value,
        GenericBag<Charset> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "12.34" })
    void sut_reuses_frozen_BigDecimal_value(
        @Freeze BigDecimal value,
        GenericBag<BigDecimal> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "16" })
    void sut_reuses_frozen_BigInteger_value(
        @Freeze BigInteger value,
        GenericBag<BigInteger> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "src/test/resources/test.csv" })
    void sut_reuses_frozen_File_value(
        @Freeze File value,
        GenericBag<File> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @CsvAutoSource({ "autoparams.CsvAutoSource" })
    void sut_reuses_frozen_Class_value(
        @Freeze Class<?> value,
        GenericBag<Class<?>> bag
    ) {
        assertSame(value, bag.getValue());
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_creates_instance(
        String[] value,
        String textBlock,
        boolean useHeadersInDisplayName,
        char quoteCharacter,
        char delimiter,
        String delimiterString,
        String emptyValue,
        String[] nullValues,
        int maxCharsPerColumn,
        boolean ignoreLeadingAndTrailingWhitespace
    ) {
        CsvAutoSource actual = CsvAutoSource.ProxyFactory.create(
            value,
            textBlock,
            useHeadersInDisplayName,
            quoteCharacter,
            delimiter,
            delimiterString,
            emptyValue,
            nullValues,
            maxCharsPerColumn,
            ignoreLeadingAndTrailingWhitespace
        );

        assertNotNull(actual);
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_correctly_configures_annotationType(
        String[] value,
        String textBlock,
        boolean useHeadersInDisplayName,
        char quoteCharacter,
        char delimiter,
        String delimiterString,
        String emptyValue,
        String[] nullValues,
        int maxCharsPerColumn,
        boolean ignoreLeadingAndTrailingWhitespace
    ) {
        CsvAutoSource actual = CsvAutoSource.ProxyFactory.create(
            value,
            textBlock,
            useHeadersInDisplayName,
            quoteCharacter,
            delimiter,
            delimiterString,
            emptyValue,
            nullValues,
            maxCharsPerColumn,
            ignoreLeadingAndTrailingWhitespace
        );

        assertEquals(CsvAutoSource.class, actual.annotationType());
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_correctly_configures_properties(
        String[] value,
        String textBlock,
        boolean useHeadersInDisplayName,
        char quoteCharacter,
        char delimiter,
        String delimiterString,
        String emptyValue,
        String[] nullValues,
        int maxCharsPerColumn,
        boolean ignoreLeadingAndTrailingWhitespace
    ) {
        CsvAutoSource actual = CsvAutoSource.ProxyFactory.create(
            value,
            textBlock,
            useHeadersInDisplayName,
            quoteCharacter,
            delimiter,
            delimiterString,
            emptyValue,
            nullValues,
            maxCharsPerColumn,
            ignoreLeadingAndTrailingWhitespace
        );

        assertEquals(value, actual.value());
        assertEquals(textBlock, actual.textBlock());
        assertEquals(useHeadersInDisplayName, actual.useHeadersInDisplayName());
        assertEquals(quoteCharacter, actual.quoteCharacter());
        assertEquals(delimiter, actual.delimiter());
        assertEquals(delimiterString, actual.delimiterString());
        assertEquals(emptyValue, actual.emptyValue());
        assertEquals(nullValues, actual.nullValues());
        assertEquals(maxCharsPerColumn, actual.maxCharsPerColumn());
        assertEquals(
            ignoreLeadingAndTrailingWhitespace,
            actual.ignoreLeadingAndTrailingWhitespace()
        );
    }
}
