package test.autoparams

import autoparams.AutoSource
import autoparams.customization.Customization
import autoparams.kotlin.AutoKotlinSource
import autoparams.lombok.BuilderCustomizer
import autoparams.mockito.MockitoCustomizer
import org.junit.jupiter.params.ParameterizedTest

class DependencyTests {

    @ParameterizedTest
    @AutoSource
    fun `AutoSource installed`(x: Int) {
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `AutoKotlinSource installed`(x: Int) {
    }

    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer::class)
    fun `BuilderCustomizer installed`(x: Int) {
    }
    
    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer::class)
    fun `MockitoCustomizer installed`(x: Int) {
    }
}
