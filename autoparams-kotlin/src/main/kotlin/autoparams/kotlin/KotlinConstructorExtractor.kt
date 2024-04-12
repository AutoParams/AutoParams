package autoparams.kotlin

import java.lang.reflect.Constructor
import autoparams.generator.ConstructorExtractor
import kotlin.reflect.jvm.kotlinFunction

internal class KotlinConstructorExtractor : ConstructorExtractor {

    override fun extract(type: Class<*>): MutableCollection<Constructor<*>> {
        return type
            .constructors
            .filter { it.parameterCount > 0 || it.kotlinFunction != null }
            .toMutableList()
    }
}
