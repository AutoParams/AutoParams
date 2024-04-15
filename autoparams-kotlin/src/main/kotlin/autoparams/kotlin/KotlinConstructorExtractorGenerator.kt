package autoparams.kotlin

import autoparams.ResolutionContext
import autoparams.generator.ConstructorExtractor
import autoparams.generator.ObjectGeneratorBase
import autoparams.generator.ObjectQuery

internal class KotlinConstructorExtractorGenerator :
    ObjectGeneratorBase<ConstructorExtractor>() {

    override fun generateObject(
        query: ObjectQuery,
        context: ResolutionContext
    ): ConstructorExtractor = KotlinConstructorExtractor()
}
