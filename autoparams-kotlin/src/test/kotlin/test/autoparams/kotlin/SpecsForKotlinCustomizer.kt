package test.autoparams.kotlin

import autoparams.AutoSource
import autoparams.customization.Customization
import autoparams.kotlin.KotlinCustomizer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest

class SpecsForKotlinCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(KotlinCustomizer::class)
    fun `SUT generates arbitrary arguments for constructor in which all parameters have default values`(
        bag: DataBagWithDefaultArguments
    ) {
        val defaults = DataBagWithDefaultArguments()
        assertThat(bag.value1).isNotEqualTo(defaults.value1)
        assertThat(bag.value2).isNotEqualTo(defaults.value2)
        assertThat(bag.value3).isNotEqualTo(defaults.value3)
    }
}
