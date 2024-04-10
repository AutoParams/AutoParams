package autoparams.kotlin

import autoparams.customization.CompositeCustomizer

class KotlinCustomizer : CompositeCustomizer(
    KotlinConstructorExtractorCustomizer(),
    KotlinClassCustomizer()
)
