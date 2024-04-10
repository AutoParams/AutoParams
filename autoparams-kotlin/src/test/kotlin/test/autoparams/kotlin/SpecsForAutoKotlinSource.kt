package test.autoparams.kotlin

import autoparams.kotlin.AutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import kotlin.reflect.KClass

class SpecsForAutoKotlinSource {

    @ParameterizedTest
    @AutoKotlinSource
    fun `SUT generates arbitrary arguments for constructor in which all parameters have default values`(
        bag: DataBagWithDefaultArguments
    ) {
        val defaults = DataBagWithDefaultArguments()
        assertThat(bag.value1).isNotEqualTo(defaults.value1)
        assertThat(bag.value2).isNotEqualTo(defaults.value2)
        assertThat(bag.value3).isNotEqualTo(defaults.value3)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `SUT generates KClass arguments`(
        arg: KClass<*>
    ) {
        assertThat(arg).isNotNull
    }
}
