package autoparams.kotlin

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest

class SpecsForAutoKotlinSource {

    @ParameterizedTest
    @AutoKotlinSource
    fun `SUT generates arbitrary arguments for constructor in which all parameters have default values`(
        generated: AllParametersHaveDefaultArguments
    ) {
        val constructed = AllParametersHaveDefaultArguments()
        Assertions.assertThat(generated.value1).isNotEqualTo(constructed.value1)
        Assertions.assertThat(generated.value2).isNotEqualTo(constructed.value2)
        Assertions.assertThat(generated.value3).isNotEqualTo(constructed.value3)
    }
}
