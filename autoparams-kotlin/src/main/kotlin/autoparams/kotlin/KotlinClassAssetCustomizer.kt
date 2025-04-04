package autoparams.kotlin

import autoparams.AssetConverter
import autoparams.ResolutionContext
import autoparams.customization.Customizer
import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerator

internal class KotlinClassAssetCustomizer : Customizer {

    override fun customize(generator: ObjectGenerator): ObjectGenerator {
        return ObjectGenerator { query, context ->
            when (query.type) {
                AssetConverter::class.java -> generate(generator, context)
                else -> generator.generate(query, context)
            }
        }
    }

    private fun generate(
        generator: ObjectGenerator,
        context: ResolutionContext
    ): ObjectContainer = generator
        .generate(AssetConverter::class.java, context)
        .process { value -> KotlinClassAssetConverter(value as AssetConverter) }
}
