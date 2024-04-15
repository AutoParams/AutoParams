package test.autoparams;

import autoparams.CsvAutoSource;
import autoparams.customization.Fix;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
class SpecsForCsvAutoSourceWithMultiLineTextBlock {

    @ParameterizedTest
    @CsvAutoSource(textBlock = """
        # FRUIT,       RANK
        apple,         1
        """)
    void sut_correctly_works_with_textBlock(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvAutoSource(useHeadersInDisplayName = true, textBlock = """
        FRUIT,       RANK
        apple,         1
        """)
    void sut_correctly_works_with_useHeadersInDisplayName(
        String fruit,
        int rank,
        String anonymous,
        TestInfo testInfo
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
        assertThat(testInfo.getDisplayName())
            .startsWith("[1] FRUIT = apple, RANK = 1");
    }

    public record StringBag(String value) {
    }

    @ParameterizedTest
    @CsvAutoSource(useHeadersInDisplayName = true, textBlock = """
        FRUIT,       RANK
        apple,         1
        """)
    void useHeadersInDisplayName_correctly_works_with_ArgumentProcessor(
        @Fix String fruit,
        int rank,
        StringBag bag
    ) {
        assertThat(bag.value()).isEqualTo(fruit);
    }

    @ParameterizedTest
    @CsvAutoSource(quoteCharacter = '%', textBlock = """
        # FRUIT,       RANK
        %apple%,         1
        """)
    void sut_correctly_works_with_quoteCharacter(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }

    @ParameterizedTest
    @CsvAutoSource(delimiter = ';', textBlock = """
        # FRUIT;       RANK
        apple;         1
        """)
    void sut_correctly_works_with_delimiter(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }

    @ParameterizedTest
    @CsvAutoSource(delimiterString = ";;", textBlock = """
        # FRUIT;;       RANK
        apple;;         1
        """)
    void sut_correctly_works_with_delimiterString(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }

    @ParameterizedTest
    @CsvAutoSource(emptyValue = "EMPTY", textBlock = """
        # FRUIT,       RANK
        '',            1
        """)
    void sut_correctly_works_with_emptyValue(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isEqualTo("EMPTY");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }

    @ParameterizedTest
    @CsvAutoSource(nullValues = { "N/A", "NULL" }, textBlock = """
        # FRUIT,       RANK
        NULL,          1
        N/A,           2
        """)
    void sut_correctly_works_with_nullValues(
        String fruit,
        int rank,
        String anonymous
    ) {
        assertThat(fruit).isNull();
        assertThat(anonymous).isNotEmpty();
    }

    // This test is disabled because it fails if the implementation is correct and otherwise passes.
    @Disabled
    @ParameterizedTest
    @CsvAutoSource(maxCharsPerColumn = 2, textBlock = """
        # FRUIT,       RANK
        apple,         1
        """)
    void sut_correctly_works_with_maxCharsPerColumn(
        String fruit,
        int rank,
        String anonymous
    ) {
    }

    @ParameterizedTest
    @CsvAutoSource(ignoreLeadingAndTrailingWhitespace = false, textBlock = """
        # RANK,    FRUIT
        1,         apple
        """)
    void sut_correctly_works_with_ignoreLeadingAndTrailingWhitespace(
        int rank,
        String fruit,
        String anonymous
    ) {
        assertThat(fruit).isNotEqualTo("apple").endsWith("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(anonymous).isNotEmpty();
    }
}
