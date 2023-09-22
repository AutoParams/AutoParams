package autoparams.kotlin

import autoparams.AutoSource
import autoparams.customization.Customization
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest

class SpecsForKotlinCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(KotlinCustomizer::class)
    fun `SUT generates arbitrary arguments for constructor in which all parameters have default values`(
        generated: AllParametersHaveDefaultArguments
    ) {
        val constructed = AllParametersHaveDefaultArguments()
        assertThat(generated.value1).isNotEqualTo(constructed.value1)
        assertThat(generated.value2).isNotEqualTo(constructed.value2)
        assertThat(generated.value3).isNotEqualTo(constructed.value3)
    }
}
