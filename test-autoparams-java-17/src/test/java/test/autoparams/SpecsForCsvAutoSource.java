package test.autoparams;

import autoparams.CsvAutoSource;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
class SpecsForCsvAutoSource {

    @ParameterizedTest
    @CsvAutoSource(textBlock = """
        # FRUIT,       RANK
        apple,         1
        """)
    void sut_correctly_works_with_textBlock(String fruit, int rank) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvAutoSource(useHeadersInDisplayName = true, textBlock = """
        FRUIT,       RANK
        apple,         1
        """)
    void sut_correctly_works_with_useHeadersInDisplayName(
        String fruit,
        int rank,
        TestInfo testInfo
    ) {
        assertThat(fruit).isEqualTo("apple");
        assertThat(rank).isEqualTo(1);
        assertThat(testInfo.getDisplayName())
            .isEqualTo("[1] FRUIT = apple, RANK = 1");
    }
}
