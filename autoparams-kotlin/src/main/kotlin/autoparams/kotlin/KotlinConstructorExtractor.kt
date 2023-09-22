package autoparams.kotlin

import autoparams.generator.ConstructorExtractor
import java.lang.reflect.Constructor
import kotlin.reflect.jvm.kotlinFunction

class KotlinConstructorExtractor: ConstructorExtractor {
    override fun extract(type: Class<*>): MutableCollection<Constructor<*>> {
        return type
            .constructors
            .filter { it.kotlinFunction != null }
            .toMutableList()
    }
}
