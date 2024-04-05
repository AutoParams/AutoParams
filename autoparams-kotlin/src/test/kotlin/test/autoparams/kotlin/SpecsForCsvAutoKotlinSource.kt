package test.autoparams.kotlin

import autoparams.kotlin.CsvAutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest

class SpecsForCsvAutoKotlinSource {

    @ParameterizedTest
    @CsvAutoKotlinSource(["1, foo"])
    fun `SUT correctly fills arguments with value property`(
        value1: Int,
        value2: String
    ) {
        assertThat(value1).isEqualTo(1)
        assertThat(value2).isEqualTo("foo")
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(["1, foo"])
    fun `SUT correctly fills extra arguments`(
        value1: Int,
        value2: String,
        value3: DataBagWithDefaultArguments
    ) {
        assertThat(value3).isNotNull()
        assertThat(value3.value2).isNotEmpty()
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        textBlock = """
        # FRUIT,       RANK
        apple,         1
        """
    )
    fun `SUT correctly fills arguments with textBlock property`(
        fruit: String,
        rank: Int
    ) {
        assertThat(fruit).isEqualTo("apple")
        assertThat(rank).isEqualTo(1)
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        useHeadersInDisplayName = true,
        textBlock = """
        FRUIT,       RANK
        apple,         1
        """
    )
    fun `SUT correctly works with useHeadersInDisplayName`(
        fruit: String,
        rank: Int,
        anonymous: String,
        testInfo: TestInfo
    ) {
        assertThat(fruit).isEqualTo("apple")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
        assertThat(testInfo.displayName)
            .startsWith("[1] FRUIT = apple, RANK = 1")
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        quoteCharacter = '%',
        textBlock = """
        # FRUIT,       RANK
        %apple%,         1
        """
    )
    fun `SUT correctly works with quoteCharacter`(
        fruit: String,
        rank: Int,
        anonymous: String
    ) {
        assertThat(fruit).isEqualTo("apple")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        delimiter = ';',
        textBlock = """
        # FRUIT;       RANK
        apple;         1
        """
    )
    fun `SUT correctly works with delimiter`(
        fruit: String,
        rank: Int,
        anonymous: String
    ) {
        assertThat(fruit).isEqualTo("apple")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        delimiterString = ";;",
        textBlock = """
        # FRUIT;;       RANK
        apple;;         1
        """
    )
    fun `SUT correctly works with delimiterString`(
        fruit: String,
        rank: Int,
        anonymous: String
    ) {
        assertThat(fruit).isEqualTo("apple")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        emptyValue = "EMPTY",
        textBlock = """
        # FRUIT,       RANK
        '',            1
        """
    )
    fun `SUT correctly works with emptyValue`(
        fruit: String,
        rank: Int,
        anonymous: String
    ) {
        assertThat(fruit).isEqualTo("EMPTY")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        nullValues = ["N/A", "NULL"],
        textBlock = """
        # FRUIT,       RANK
        NULL,          1
        N/A,           1
        """
    )
    fun `SUT correctly works with nullValues`(
        fruit: String?,
        rank: Int,
        anonymous: String
    ) {
        assertThat(fruit).isNull()
        assertThat(anonymous).isNotEmpty()
    }

    // This test is disabled because it fails if the implementation is correct and otherwise passes.
    @Disabled
    @ParameterizedTest
    @CsvAutoKotlinSource(
        maxCharsPerColumn = 2,
        textBlock = """
        # FRUIT,       RANK
        apple,         1
        """
    )
    fun `SUT correctly works with maxCharsPerColumn`(
        fruit: String,
        rank: Int,
        anonymous: String
    ) {
    }

    @ParameterizedTest
    @CsvAutoKotlinSource(
        ignoreLeadingAndTrailingWhitespace = false,
        textBlock = """
        # RANK,    FRUIT
        1,         apple
        """
    )
    fun `SUT correctly works with ignoreLeadingAndTrailingWhitespace`(
        rank: Int,
        fruit: String,
        anonymous: String
    ) {
        assertThat(fruit).isNotEqualTo("apple").endsWith("apple")
        assertThat(rank).isEqualTo(1)
        assertThat(anonymous).isNotEmpty()
    }
}
