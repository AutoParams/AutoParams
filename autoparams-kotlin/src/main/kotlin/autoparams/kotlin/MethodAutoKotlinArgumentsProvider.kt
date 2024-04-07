package autoparams.kotlin

import java.util.stream.Stream
import autoparams.MethodAutoArgumentsProvider
import autoparams.MethodAutoSource
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer

class MethodAutoKotlinArgumentsProvider :
    ArgumentsProvider,
    AnnotationConsumer<MethodAutoKotlinSource> {

    private val provider = MethodAutoArgumentsProvider()

    override fun provideArguments(
        context: ExtensionContext
    ): Stream<out Arguments> = provider.provideArguments(context)

    override fun accept(annotation: MethodAutoKotlinSource) {
        provider.accept(MethodAutoSource.ProxyFactory.create(annotation.value))
    }
}
