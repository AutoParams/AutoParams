package examples.basics.factory;

import java.util.List;

import autoparams.generator.Factory;
import autoparams.type.TypeReference;
import examples.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryCreationExamples {

    @Test
    void basicFactoryCreation() {
        // Create factory for a specific class
        Factory<Product> factory = Factory.create(Product.class);

        // Generate a product
        Product product = factory.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotEmpty();
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
        assertThat(product.getPriceAmount()).isNotNull();
        assertThat(product.getStockQuantity()).isNotNegative();
    }

    @Test
    void typeInferenceCreation() {
        // Type inference when variable type is declared
        Factory<Product> factory = Factory.create();

        // Generate a product
        Product product = factory.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotEmpty();
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
        assertThat(product.getPriceAmount()).isNotNull();
        assertThat(product.getStockQuantity()).isNotNegative();
    }

    @Test
    void genericFactoryCreation() {
        // Create factory for generic types using TypeReference
        Factory<List<Product>> factory = Factory.create(
            new TypeReference<List<Product>>() { }
        );

        // Generate a list of products
        List<Product> products = factory.get();

        assertThat(products).isNotNull();
        assertThat(products).hasSize(3); // Default collection size
        assertThat(products).allSatisfy(product -> {
            assertThat(product.getName()).isNotEmpty();
            assertThat(product.getPriceAmount()).isNotNull();
        });
    }

    @Test
    void diamondOperatorCreation() {
        // Diamond operator when variable type is declared
        Factory<List<Product>> factory = Factory.create(new TypeReference<>() { });

        // Generate a list of products
        List<Product> products = factory.get();

        assertThat(products).isNotNull();
        assertThat(products).hasSize(3); // Default collection size
        assertThat(products).allSatisfy(product -> {
            assertThat(product.getName()).isNotEmpty();
            assertThat(product.getPriceAmount()).isNotNull();
        });
    }
}
