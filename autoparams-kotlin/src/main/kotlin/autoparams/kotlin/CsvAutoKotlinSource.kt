package autoparams.kotlin

import autoparams.customization.Customization
import org.junit.jupiter.params.provider.ArgumentsSource

@ArgumentsSource(CsvAutoKotlinArgumentsProvider::class)
@Customization(KotlinCustomizer::class)
annotation class CsvAutoKotlinSource(
    val value: Array<String> = [],
    val textBlock: String = "",
    val useHeadersInDisplayName: Boolean = false,
    val quoteCharacter: Char = '\'',
    val delimiter: Char = '\u0000',
    val delimiterString: String = "",
    val emptyValue: String = "",
    val nullValues: Array<String> = [],
    val maxCharsPerColumn: Int = 4096,
    val ignoreLeadingAndTrailingWhitespace: Boolean = true
)
