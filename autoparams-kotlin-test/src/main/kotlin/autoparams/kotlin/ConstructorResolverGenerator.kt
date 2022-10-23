package autoparams.kotlin

import autoparams.generator.ConstructorResolver
import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerator
import java.beans.ConstructorProperties
import java.util.Optional
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaConstructor

internal class ConstructorResolverGenerator : ObjectGenerator {

    private val value: ObjectContainer = ObjectContainer(
        ConstructorResolver
            .compose(
                ConstructorResolver { t: Class<*> ->
                    kotlin.runCatching { t.kotlin.primaryConstructor?.javaConstructor }
                        .getOrNull()
                        .let { Optional.ofNullable(it) }
                },
                ConstructorResolver { t: Class<*> ->
                    t.constructors
                        .filter { it.isAnnotationPresent(ConstructorProperties::class.java) }
                        .minByOrNull { it.parameterCount }
                        .let { Optional.ofNullable(it) }
                },
                ConstructorResolver { t: Class<*> ->
                    t.constructors
                        .minByOrNull { it.parameterCount }
                        .let { Optional.ofNullable(it) }
                }
            )
    )

    override fun generate(
        query: autoparams.generator.ObjectQuery,
        context: autoparams.generator.ObjectGenerationContext
    ): ObjectContainer {
        return if (query.type === ConstructorResolver::class.java)
            value
        else
            ObjectContainer.EMPTY
    }
}
