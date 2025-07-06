package test.autoparams.customization;

import autoparams.customization.Design;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForDesign {

    @Test
    void of_creates_Design_instance_for_specified_type() {
        Design<Product> design = Design.of(Product.class);

        assertThat(design).isNotNull();
    }

    @Test
    void of_throws_exception_when_type_argument_is_null() {
        assertThatThrownBy(() -> Design.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'type' must not be null");
    }

    @Test
    void instantiate_creates_instance_of_configured_type() {
        Design<Product> design = Design.of(Product.class);

        Product product = design.instantiate();

        assertThat(product).isNotNull();
        assertThat(product).isInstanceOf(Product.class);
    }
}
