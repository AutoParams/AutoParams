package autoparams.kotlin

import java.util.stream.Stream
import autoparams.EnumAutoArgumentsProvider
import autoparams.EnumAutoSource
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer

class EnumAutoKotlinArgumentsProvider :
    ArgumentsProvider,
    AnnotationConsumer<EnumAutoKotlinSource> {

    private val provider = EnumAutoArgumentsProvider()

    override fun provideArguments(
        context: ExtensionContext
    ): Stream<out Arguments> = provider.provideArguments(context)

    override fun accept(annotation: EnumAutoKotlinSource) {
        provider.accept(
            EnumAutoSource.ProxyFactory.create(
                annotation.value.java,
                annotation.names,
                annotation.mode
            )
        )
    }
}
