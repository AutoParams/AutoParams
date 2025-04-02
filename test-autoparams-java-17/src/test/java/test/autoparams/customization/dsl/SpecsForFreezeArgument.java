package test.autoparams.customization.dsl;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;
import test.autoparams.Seller;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgumentOf;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForFreezeArgument {

    @Test
    @AutoParams
    void where_applies_predicate_correctly(
        ResolutionContext context,
        String username
    ) {
        context.applyCustomizer(
            freezeArgumentOf(String.class)
                .where(parameterNameEquals("username"))
                .to(username)
        );
        Product product = context.resolve(Product.class);
        Seller seller = context.resolve(Seller.class);
        assertThat(product.name()).isNotEqualTo(username);
        assertThat(seller.username()).isEqualTo(username);
    }
}
