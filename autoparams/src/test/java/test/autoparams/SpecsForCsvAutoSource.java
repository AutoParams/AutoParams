package test.autoparams;

import java.util.UUID;

import autoparams.AutoSource;
import autoparams.CsvAutoSource;
import autoparams.customization.Freeze;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    @CsvAutoSource({ "16, foo" })
    void sut_reuses_values_frozen_by_specified_argument(
        @Freeze int value1,
        @Freeze String value2,
        ComplexObject value3
    ) {
        assertEquals(16, value3.getValue1());
        assertEquals("foo", value3.getValue2());
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
