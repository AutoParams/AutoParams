package autoparams.kotlin

import java.lang.reflect.Proxy
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
) {

    companion object ProxyFactory {

        fun create(
            value: Array<String>,
            textBlock: String,
            useHeadersInDisplayName: Boolean,
            quoteCharacter: Char,
            delimiter: Char,
            delimiterString: String,
            emptyValue: String,
            nullValues: Array<String>,
            maxCharsPerColumn: Int,
            ignoreLeadingAndTrailingWhitespace: Boolean
        ): CsvAutoKotlinSource = Proxy.newProxyInstance(
            CsvAutoKotlinSource::class.java.classLoader,
            arrayOf(CsvAutoKotlinSource::class.java)
        ) { _, method, _ ->
            when (method.name) {
                "annotationType" -> CsvAutoKotlinSource::class.java
                "value" -> value
                "textBlock" -> textBlock
                "useHeadersInDisplayName" -> useHeadersInDisplayName
                "quoteCharacter" -> quoteCharacter
                "delimiter" -> delimiter
                "delimiterString" -> delimiterString
                "emptyValue" -> emptyValue
                "nullValues" -> nullValues
                "maxCharsPerColumn" -> maxCharsPerColumn
                "ignoreLeadingAndTrailingWhitespace" -> ignoreLeadingAndTrailingWhitespace
                else -> method.defaultValue
            }
        } as CsvAutoKotlinSource
    }
}
