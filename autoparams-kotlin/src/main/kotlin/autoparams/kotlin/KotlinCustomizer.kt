package autoparams.kotlin

import autoparams.customization.CompositeCustomizer

/**
 * A customizer that enhances AutoParams to work effectively with
 * Kotlin-specific features and types.
 *
 * This customizer is automatically applied when using the [AutoKotlinParams]
 * annotation or the [AutoKotlinSource] annotation, enabling AutoParams to
 * correctly generate test values for Kotlin classes with features like default
 * parameter values, data classes, and named parameters.
 *
 * @see AutoKotlinParams
 */
class KotlinCustomizer : CompositeCustomizer(
    KotlinConstructorExtractorGenerator(),
    KotlinClassGenerator(),
    KotlinClassAssetCustomizer()
)
