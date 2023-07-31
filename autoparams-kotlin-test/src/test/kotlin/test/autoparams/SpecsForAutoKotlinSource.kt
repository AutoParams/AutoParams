package test.autoparams

import autoparams.AutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest

class SpecsForAutoKotlinSource {

    @Test
    fun `SUT is decorated with @Target`() {
        val annotations: List<Annotation> = AutoKotlinSource::class.annotations
        val actual: Target? = annotations.singleOrNull { x -> x is Target } as Target?
        assertThat(actual).isNotNull
        assertThat(actual!!.allowedTargets).contains(
            AnnotationTarget.ANNOTATION_CLASS,
            AnnotationTarget.FUNCTION)
    }

    @Test
    fun `SUT is decorated with @Retention`() {
        val annotations: List<Annotation> = AutoKotlinSource::class.annotations
        val actual: Retention? = annotations.singleOrNull { x -> x is Retention } as Retention?
        assertThat(actual).isNotNull
        assertThat(actual!!.value).isEqualTo(AnnotationRetention.RUNTIME)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `SUT creates arbitrary arguments`(
        a1: String,
        a2: String,
        a3: String
    ) {
        assertThat(a1).isNotEqualTo(a2)
        assertThat(a2).isNotEqualTo(a1)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `SUT generates arbitrary arguments for constructors where all parameters have default arguments`(
        generated: AllParametersHaveDefaultArguments
    ) {
        val constructed = AllParametersHaveDefaultArguments()
        assertThat(generated.value1).isNotEqualTo(constructed.value1)
        assertThat(generated.value2).isNotEqualTo(constructed.value2)
        assertThat(generated.value3).isNotEqualTo(constructed.value3)
    }
}
