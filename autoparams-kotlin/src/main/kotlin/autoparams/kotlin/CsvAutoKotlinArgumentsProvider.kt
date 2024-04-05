package autoparams.kotlin

import java.lang.reflect.Proxy
import java.util.stream.Stream
import autoparams.CsvAutoArgumentsProvider
import autoparams.CsvAutoSource
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer

class CsvAutoKotlinArgumentsProvider :
    ArgumentsProvider,
    AnnotationConsumer<CsvAutoKotlinSource> {

    private val provider = CsvAutoArgumentsProvider()

    override fun provideArguments(
        context: ExtensionContext?
    ): Stream<out Arguments> {
        return provider.provideArguments(context)
    }

    override fun accept(annotation: CsvAutoKotlinSource) {
        provider.accept(Proxy.newProxyInstance(
            CsvAutoSource::class.java.classLoader,
            arrayOf(CsvAutoSource::class.java)
        ) { _, method, _ ->
            when {
                method.name.equals("value") -> annotation.value
                method.name.equals("textBlock") -> annotation.textBlock.trimIndent()
                method.name.equals("useHeadersInDisplayName") -> annotation.useHeadersInDisplayName
                method.name.equals("quoteCharacter") -> annotation.quoteCharacter
                method.name.equals("delimiter") -> annotation.delimiter
                method.name.equals("delimiterString") -> annotation.delimiterString
                method.name.equals("emptyValue") -> annotation.emptyValue
                method.name.equals("nullValues") -> annotation.nullValues
                method.name.equals("maxCharsPerColumn") -> annotation.maxCharsPerColumn
                method.name.equals("ignoreLeadingAndTrailingWhitespace") ->
                    annotation.ignoreLeadingAndTrailingWhitespace

                else -> method.defaultValue
            }
        } as CsvAutoSource)
    }
}
