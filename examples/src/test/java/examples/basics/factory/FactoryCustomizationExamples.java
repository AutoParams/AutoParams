package examples.basics.factory;

import autoparams.generator.Factory;
import examples.Product;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
import static org.assertj.core.api.Assertions.assertThat;

public class FactoryCustomizationExamples {

    @Test
    void customizeSpecificProperty() {
        // Customize a specific property using DSL
        Factory<Product> factory = Factory.create();
        Product product = factory.get(
            set(Product::getName).to("Custom Product Name")
        );

        assertThat(product.getName()).isEqualTo("Custom Product Name");
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
        assertThat(product.getPrice()).isNotNegative();
        assertThat(product.getStock()).isNotNegative();
    }

    @Test
    void customizeMultipleProperties() {
        // Customize multiple properties at once
        Factory<Product> factory = Factory.create();
        Product product = factory.get(
            set(Product::getName).to("Premium Product"),
            set(Product::getPrice).to(999.99),
            set(Product::getStock).to(100)
        );

        assertThat(product.getName()).isEqualTo("Premium Product");
        assertThat(product.getPrice()).isEqualTo(999.99);
        assertThat(product.getStock()).isEqualTo(100);
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
    }

    @Test
    void customizeWithGetRange() {
        // Apply customizations to multiple objects
        Factory<Product> factory = Factory.create();
        var products = factory.get(
            3,
            set(Product::getName).to("Bulk Product"),
            set(Product::getStock).to(50)
        );

        assertThat(products).hasSize(3);
        assertThat(products).allSatisfy(product -> {
            assertThat(product.getName()).isEqualTo("Bulk Product");
            assertThat(product.getStock()).isEqualTo(50);
            assertThat(product.getPrice()).isNotNegative();
        });
    }
}
