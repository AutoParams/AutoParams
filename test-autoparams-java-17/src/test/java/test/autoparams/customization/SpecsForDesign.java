package test.autoparams.customization;

import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    void supply_configures_property_value_using_supplier() {
        Design<Product> design = Design.of(Product.class)
            .supply(Product::stockQuantity, () -> 42);

        Product product = design.instantiate();

        assertThat(product.stockQuantity()).isEqualTo(42);
    }

    @Test
    void supply_calls_supplier_only_when_instantiate_is_called() {
        AtomicInteger callCount = new AtomicInteger(0);
        Design<Product> design = Design.of(Product.class)
            .supply(Product::stockQuantity, () -> {
                callCount.incrementAndGet();
                return 42;
            });

        assertThat(callCount.get()).isEqualTo(0);

        design.instantiate();

        assertThat(callCount.get()).isEqualTo(1);
    }

    @Test
    void supply_throws_exception_when_propertyGetter_is_null() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.supply(null, () -> 42))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'propertyGetter' must not be null");
    }

    @Test
    void supply_throws_exception_when_supplier_is_null() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.supply(Product::stockQuantity, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'supplier' must not be null");
    }
}
