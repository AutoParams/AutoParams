package autoparams.kotlin

import autoparams.AutoSource
import autoparams.customization.Customization

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@AutoSource
@Customization(KotlinCustomizer::class)
annotation class AutoKotlinSource
