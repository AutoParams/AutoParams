package test.autoparams.customization;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import autoparams.customization.Design;
import org.junit.jupiter.api.Test;
import test.autoparams.Category;
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

    @Test
    void set_configures_property_value_for_instantiated_object() {
        Design<Product> design = Design.of(Product.class)
            .set(Product::name, "Test Product");

        Product product = design.instantiate();

        assertThat(product.name()).isEqualTo("Test Product");
    }

    @Test
    void set_allows_chaining_multiple_property_configurations() {
        Design<Product> design = Design.of(Product.class)
            .set(Product::name, "Test Product")
            .set(Product::imageUri, "https://example.com/product.jpg");

        Product product = design.instantiate();

        assertThat(product.name()).isEqualTo("Test Product");
        assertThat(product.imageUri()).isEqualTo("https://example.com/product.jpg");
    }

    @Test
    void set_throws_exception_when_propertyGetter_is_null() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.set(null, "Test Product"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'propertyGetter' must not be null");
    }

    @Test
    void set_returns_new_Design_instance() {
        Design<Product> original = Design.of(Product.class);

        Design<Product> modified = original.set(Product::name, "Test Product");

        assertThat(modified).isNotSameAs(original);
        assertThat(modified).isNotNull();
    }

    @Test
    void design_configures_nested_object_property_values() {
        Design<Product> design = Design.of(Product.class)
            .design(Product::category, category -> category
                .set(Category::name, "Electronics")
            );

        Product product = design.instantiate();

        assertThat(product.category().name()).isEqualTo("Electronics");
    }

    @Test
    void design_calls_design_function_only_when_instantiate_is_called() {
        AtomicInteger callCount = new AtomicInteger(0);
        Design<Product> design = Design.of(Product.class)
            .design(Product::category, category -> {
                callCount.incrementAndGet();
                return category.set(Category::name, "Electronics");
            });

        assertThat(callCount.get()).isEqualTo(0);

        design.instantiate();

        assertThat(callCount.get()).isEqualTo(1);
    }

    @Test
    void design_throws_exception_when_propertyGetter_is_null() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.design(null, (Design<Category> category) ->
            category.set(Category::name, "Electronics")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'propertyGetter' must not be null");
    }

    @Test
    void design_throws_exception_when_designFunction_is_null() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.design(Product::category, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'designFunction' must not be null");
    }

    @Test
    void instantiate_with_count_creates_list_with_specified_number_of_instances() {
        Design<Product> design = Design.of(Product.class);

        List<Product> products = design.instantiate(3);

        assertThat(products).hasSize(3);
        assertThat(products).allMatch(product -> product != null);
        assertThat(products).allMatch(product -> product instanceof Product);
    }

    @Test
    void instantiate_with_count_applies_configurations_to_all_instances() {
        Design<Product> design = Design.of(Product.class)
            .set(Product::name, "Test Product")
            .set(Product::stockQuantity, 100);

        List<Product> products = design.instantiate(3);

        assertThat(products).hasSize(3);
        assertThat(products).allMatch(product -> product.name().equals("Test Product"));
        assertThat(products).allMatch(product -> product.stockQuantity() == 100);
    }

    @Test
    void instantiate_with_count_throws_exception_when_count_is_negative() {
        Design<Product> design = Design.of(Product.class);

        assertThatThrownBy(() -> design.instantiate(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'count' must not be less than 0");
    }

    @Test
    void instantiate_with_count_creates_unique_instances() {
        Design<Product> design = Design.of(Product.class);

        List<Product> products = design.instantiate(3);

        assertThat(products).hasSize(3);
        assertThat(products.get(0)).isNotSameAs(products.get(1));
        assertThat(products.get(1)).isNotSameAs(products.get(2));
        assertThat(products.get(0)).isNotSameAs(products.get(2));
    }

    @Test
    void instantiate_with_count_returns_unmodifiable_list() {
        Design<Product> design = Design.of(Product.class);

        List<Product> products = design.instantiate(2);

        assertThatThrownBy(() -> products.add(design.instantiate()))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
