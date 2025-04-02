package test.autoparams;

import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForResolutionContext {

    @Test
    @AutoParams
    void customize_applies_customizers_correctly(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        sut.customize(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.resolve(Product.class);

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void branch_applies_customizers_correctly(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        ResolutionContext context = sut.branch(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = context.resolve(Product.class);

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void branch_does_not_affect_original_context(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        sut.branch(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.resolve(Product.class);

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }
}
