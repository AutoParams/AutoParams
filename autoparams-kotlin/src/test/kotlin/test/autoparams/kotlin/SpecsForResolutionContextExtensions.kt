package test.autoparams.kotlin

import autoparams.ResolutionContext
import autoparams.generator.ObjectContainer
import autoparams.kotlin.AutoKotlinParams
import autoparams.kotlin.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpecsForResolutionContextExtensions {

    @Test
    @AutoKotlinParams
    fun `generate correctly returns value of reified type`(value: Int) {
        val sut = ResolutionContext()
        sut.applyCustomizer { _, _ -> ObjectContainer(value) }

        val actual: Int = sut.resolve()

        assertThat(actual).isEqualTo(value)
    }

    @Test
    @AutoKotlinParams
    fun `resolve correctly returns value of reified type`(value: Int) {
        val sut = ResolutionContext()
        sut.applyCustomizer { _, _ -> ObjectContainer(value) }

        val actual: Int = sut.resolve()

        assertThat(actual).isEqualTo(value)
    }
}
