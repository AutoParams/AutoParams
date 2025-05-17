package test.autoparams.customization.dsl

import autoparams.ResolutionContext
import autoparams.customization.dsl.ArgumentCustomizationDsl.set
import autoparams.kotlin.AutoKotlinParams
import jakarta.validation.constraints.Max
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import test.autoparams.Product

class SpecsForInstalledDsl {

    @Test
    @AutoKotlinParams
    fun `set correctly sets arguments`(
        context: ResolutionContext,
        name: String,
        @Max(100) stockQuantity: Int
    ) {
        context.customize(
            set(Product::name).to(name),
            set(Product::stockQuantity).to(stockQuantity)
        )

        val product: Product = context.resolve()

        assertThat(product.name).isEqualTo(name)
        assertThat(product.stockQuantity).isEqualTo(stockQuantity)
    }
}
