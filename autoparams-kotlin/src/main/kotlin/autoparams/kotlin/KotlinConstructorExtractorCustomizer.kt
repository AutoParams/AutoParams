package autoparams.kotlin

import autoparams.customization.Customizer
import autoparams.generator.ConstructorExtractor
import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerator

internal class KotlinConstructorExtractorCustomizer : Customizer {

    override fun customize(generator: ObjectGenerator): ObjectGenerator =
        ObjectGenerator { query, context ->
            if (query.type.equals(ConstructorExtractor::class.java))
                ObjectContainer(KotlinConstructorExtractor())
            else generator.generate(query, context)
        }
}
