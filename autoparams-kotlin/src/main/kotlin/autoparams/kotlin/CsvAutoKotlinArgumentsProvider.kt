package autoparams.kotlin

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
        context: ExtensionContext
    ): Stream<out Arguments> = provider.provideArguments(context)

    override fun accept(annotation: CsvAutoKotlinSource) {
        provider.accept(
            CsvAutoSource.ProxyFactory.create(
                annotation.value,
                annotation.textBlock.trimIndent(),
                annotation.useHeadersInDisplayName,
                annotation.quoteCharacter,
                annotation.delimiter,
                annotation.delimiterString,
                annotation.emptyValue,
                annotation.nullValues,
                annotation.maxCharsPerColumn,
                annotation.ignoreLeadingAndTrailingWhitespace
            )
        )
    }
}
