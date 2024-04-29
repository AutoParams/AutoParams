package autoparams.kotlin

import autoparams.ObjectQuery
import autoparams.ResolutionContext
import autoparams.generator.ConstructorExtractor
import autoparams.generator.ObjectGeneratorBase

internal class KotlinConstructorExtractorGenerator :
    ObjectGeneratorBase<ConstructorExtractor>() {

    override fun generateObject(
        query: ObjectQuery,
        context: ResolutionContext
    ): ConstructorExtractor = KotlinConstructorExtractor()
}
