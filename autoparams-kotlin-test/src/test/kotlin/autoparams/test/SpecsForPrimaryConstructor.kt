package autoparams.test

import autoparams.AutoSource
import autoparams.customization.Customization
import autoparams.kotlin.KotlinCustomizer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest

internal class SpecsForPrimaryConstructor {

    data class HavingDefaultValueForConstructorParameter(
        val value1: String = "default value",
        val value2: List<Int> = emptyList(),
        val value3: Long = 0,
    )

    @ParameterizedTest
    @AutoSource
    @Customization(KotlinCustomizer::class)
    internal fun sut_creates_with_primary_constructor(fixture: HavingDefaultValueForConstructorParameter) {
        val defaultValue = HavingDefaultValueForConstructorParameter()
        assertThat(fixture.value1).isNotEqualTo(defaultValue.value1)
        assertThat(fixture.value2).isNotEqualTo(defaultValue.value2)
        assertThat(fixture.value3).isNotEqualTo(defaultValue.value3)
    }

}
