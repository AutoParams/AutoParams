package test.autoparams;

import autoparams.CsvAutoSource;
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
}
