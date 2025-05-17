package test.autoparams

import autoparams.AutoParams
import autoparams.customization.Customization
import autoparams.kotlin.AutoKotlinParams
import autoparams.lombok.BuilderCustomizer
import autoparams.mockito.MockitoCustomizer
import org.junit.jupiter.api.Test

class DependencyTests {

    @Test
    @AutoParams
    fun `AutoParams installed`(x: Int) {
    }

    @Test
    @AutoKotlinParams
    fun `AutoKotlinParams installed`(x: Int) {
    }

    @Test
    @AutoParams
    @Customization(BuilderCustomizer::class)
    fun `BuilderCustomizer installed`(x: Int) {
    }

    @Test
    @AutoParams
    @Customization(MockitoCustomizer::class)
    fun `MockitoCustomizer installed`(x: Int) {
    }
}
