package examples.basics.factory;

import java.util.List;
import java.util.stream.Collectors;

import autoparams.generator.Factory;
import examples.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryGenerationExamples {

    @Test
    void singleObjectGeneration() {
        // Generate a single object using get()
        Factory<Product> factory = Factory.create();
        Product product = factory.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotEmpty();
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
        assertThat(product.getPrice()).isNotNegative();
        assertThat(product.getStock()).isNotNegative();
    }

    @Test
    void multipleObjectGeneration() {
        // Generate multiple objects using get(count)
        Factory<Product> factory = Factory.create();
        List<Product> products = factory.get(5);

        assertThat(products).hasSize(5);
        assertThat(products).allSatisfy(product -> {
            assertThat(product).isNotNull();
            assertThat(product.getName()).isNotEmpty();
            assertThat(product.getPrice()).isNotNegative();
        });

        // Each product should be unique
        assertThat(products).doesNotHaveDuplicates();
    }

    @Test
    void streamGeneration() {
        // Generate objects using stream()
        Factory<Product> factory = Factory.create();

        List<Product> products = factory.stream()
            .limit(10)
            .collect(Collectors.toList());

        assertThat(products).hasSize(10);
        assertThat(products).allSatisfy(product -> {
            assertThat(product).isNotNull();
            assertThat(product.getName()).isNotEmpty();
            assertThat(product.getPrice()).isNotNegative();
        });

        // Each product should be unique
        assertThat(products).doesNotHaveDuplicates();
    }
}
