package autoparams.kotlin

import autoparams.generator.ObjectContainer
import autoparams.generator.ObjectGenerationContext
import autoparams.generator.ObjectGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest

class SpecsForObjectGenerationContextExtensions {

    @ParameterizedTest
    @AutoKotlinSource
    fun `generate correctly returns value of reified type`(
        extensionContext: ExtensionContext,
        value: Int,
    ) {
        val sut = ObjectGenerationContext(
            extensionContext,
            ObjectGenerator { _, _ -> ObjectContainer(value) },
        )

        val actual: Int = sut.generate()

        assertThat(actual).isEqualTo(value)
    }
}
