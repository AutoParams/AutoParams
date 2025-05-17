package test.autoparams.customization.dsl

import autoparams.ResolutionContext
import autoparams.customization.dsl.ArgumentCustomizationDsl.set
import autoparams.kotlin.AutoKotlinParams
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import test.autoparams.Product

class SpecsForDslInKotlin {

    @Test
    @AutoKotlinParams
    fun `set correctly sets argument`(
        context: ResolutionContext,
        name: String
    ) {
        context.customize(set(Product::name).to(name))
        val product: Product = context.resolve()
        assertThat(product.name).isEqualTo(name)
    }
}
