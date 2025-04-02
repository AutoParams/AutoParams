package test.autoparams;

import java.util.UUID;

import autoparams.AutoParams;
import autoparams.generator.Factory;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForFactory {

    @Test
    @AutoParams
    void get_applies_customizers_correctly(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        Product product = sut.get(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );
        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void get_applies_customizers_one_time(
        Factory<Product> sut,
        UUID id,
        int stockQuantity
    ) {
        sut.get(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.get();

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }
}
