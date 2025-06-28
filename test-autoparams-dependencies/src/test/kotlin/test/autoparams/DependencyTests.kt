package test.autoparams

import autoparams.AutoParams
import autoparams.LogResolution
import autoparams.customization.Customization
import autoparams.kotlin.AutoKotlinParams
import autoparams.lombok.BuilderCustomizer
import autoparams.mockito.MockitoCustomizer
import org.junit.jupiter.api.Test

@Suppress("UNUSED_PARAMETER")
class DependencyTests {

    @Test
    @AutoParams
    @LogResolution
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
