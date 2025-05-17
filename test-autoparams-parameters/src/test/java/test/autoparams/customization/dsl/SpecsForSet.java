package test.autoparams.customization.dsl;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import test.autoparams.Category;
import test.autoparams.Product;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForSet {

    @Test
    @AutoParams
    void set_correctly_sets_argument(
        ResolutionContext context,
        String name
    ) {
        context.customize(set(Product::getName).to(name));
        Product product = context.resolve();
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    @AutoParams
    void set_does_not_set_other_arguments(
        ResolutionContext context,
        String name
    ) {
        context.customize(set(Product::getName).to(name));
        Product product = context.resolve();
        assertThat(product.getDescription()).isNotEqualTo(name);
    }

    @RepeatedTest(10)
    @AutoParams
    void set_correctly_sets_argument_with_is_prefix(
        ResolutionContext context,
        boolean displayed
    ) {
        context.customize(set(Product::isDisplayed).to(displayed));
        Product product = context.resolve();
        assertThat(product.isDisplayed()).isEqualTo(displayed);
    }

    @Test
    @AutoParams
    void set_does_not_set_argument_of_other_type(
        ResolutionContext context,
        String name
    ) {
        context.customize(set(Product::getName).to(name));
        Category category = context.resolve();
        assertThat(category.getName()).isNotEqualTo(name);
    }
}
