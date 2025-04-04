package autoparams.kotlin

import autoparams.AutoParams
import autoparams.customization.Customization

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@AutoParams
@Customization(KotlinCustomizer::class)
annotation class AutoKotlinParams
