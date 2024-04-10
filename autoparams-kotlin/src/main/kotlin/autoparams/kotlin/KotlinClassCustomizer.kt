package autoparams.kotlin

import java.lang.reflect.ParameterizedType
import autoparams.customization.Customizer
import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerator
import kotlin.reflect.KClass

internal class KotlinClassCustomizer : Customizer {

    override fun customize(generator: ObjectGenerator): ObjectGenerator {
        return ObjectGenerator { query, context ->
            when (query.type) {
                is ParameterizedType ->
                    when ((query.type as ParameterizedType).rawType) {
                        KClass::class.java -> ObjectContainer(String::class)
                        else -> generator.generate(query, context)
                    }

                else -> generator.generate(query, context)
            }
        }
    }
}
