package autoparams.kotlin

import autoparams.customization.CompositeCustomizer

class KotlinCustomizer : CompositeCustomizer(
    KotlinConstructorExtractorGenerator(),
    KotlinClassGenerator(),
    KotlinClassAssetCustomizer()
)
