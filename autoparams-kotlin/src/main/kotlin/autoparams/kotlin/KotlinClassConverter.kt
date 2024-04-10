package autoparams.kotlin

import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.converter.ArgumentConverter
import kotlin.reflect.KClass

internal class KotlinClassConverter(
    private val defaultConverter: ArgumentConverter
) : ArgumentConverter {

    override fun convert(source: Any?, context: ParameterContext): Any? = if (
        context.parameter.type.equals(KClass::class.java) &&
        source is Class<*>
    ) {
        source.kotlin
    } else {
        defaultConverter.convert(source, context)
    }
}
