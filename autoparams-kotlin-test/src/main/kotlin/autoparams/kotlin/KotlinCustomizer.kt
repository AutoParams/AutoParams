package autoparams.kotlin

import autoparams.customization.Customizer
import autoparams.generator.CompositeObjectGenerator
import autoparams.generator.ObjectGenerator

class KotlinCustomizer : Customizer {
    override fun customize(generator: ObjectGenerator): ObjectGenerator {
        return CompositeObjectGenerator(
            ConstructorResolverGenerator(),
            generator,
        )
    }
}
