package autoparams.kotlin

import java.lang.reflect.ParameterizedType
import autoparams.ObjectQuery
import autoparams.ResolutionContext
import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerator
import kotlin.reflect.KClass

internal class KotlinClassGenerator : ObjectGenerator {

    override fun generate(
        query: ObjectQuery,
        context: ResolutionContext
    ): ObjectContainer {
        return if (query.type is ParameterizedType) {
            when ((query.type as ParameterizedType).rawType) {
                KClass::class.java -> ObjectContainer(String::class)
                else -> ObjectContainer.EMPTY
            }
        } else {
            ObjectContainer.EMPTY
        }
    }
}
