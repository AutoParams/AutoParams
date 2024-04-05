package test.autoparams.kotlin

import autoparams.ResolutionContext
import autoparams.generator.ObjectContainer
import autoparams.kotlin.AutoKotlinSource
import autoparams.kotlin.generate
import autoparams.kotlin.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest

class SpecsForResolutionContextExtensions {

    @Suppress("DEPRECATION")
    @ParameterizedTest
    @AutoKotlinSource
    fun `generate correctly returns value of reified type`(
        extensionContext: ExtensionContext,
        value: Int,
    ) {
        val sut = ResolutionContext(
            extensionContext,
            { _, _ -> ObjectContainer(value) },
            { _, _, _ -> }
        )

        val actual: Int = sut.generate()

        assertThat(actual).isEqualTo(value)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `resolve correctly returns value of reified type`(
        extensionContext: ExtensionContext,
        value: Int,
    ) {
        val sut = ResolutionContext(
            extensionContext,
            { _, _ -> ObjectContainer(value) },
            { _, _, _ -> }
        )

        val actual: Int = sut.resolve()

        assertThat(actual).isEqualTo(value)
    }
}
