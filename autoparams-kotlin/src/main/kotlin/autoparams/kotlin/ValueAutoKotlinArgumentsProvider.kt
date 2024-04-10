package autoparams.kotlin

import java.util.stream.Stream
import autoparams.ValueAutoArgumentsProvider
import autoparams.ValueAutoSource
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer

class ValueAutoKotlinArgumentsProvider :
    ArgumentsProvider,
    AnnotationConsumer<ValueAutoKotlinSource> {

    private val provider = ValueAutoArgumentsProvider()

    override fun provideArguments(
        context: ExtensionContext?
    ): Stream<out Arguments> = provider.provideArguments(context)

    override fun accept(annotation: ValueAutoKotlinSource) {
        provider.accept(
            ValueAutoSource.ProxyFactory.create(
                annotation.shorts,
                annotation.bytes,
                annotation.ints,
                annotation.longs,
                annotation.floats,
                annotation.doubles,
                annotation.chars,
                annotation.booleans,
                annotation.strings,
                annotation.classes.map { it.java }.toTypedArray()
            )
        )
    }
}
