package autoparams.kotlin

import autoparams.ResolutionContext
import autoparams.generator.ObjectContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest

class SpecsForResolutionContextExtensions {

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
}
