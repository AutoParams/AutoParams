package examples.basics.factory;

import java.util.List;

import autoparams.AutoParams;
import autoparams.generator.Factory;
import examples.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryInjectionExamples {

    @Test
    @AutoParams
    void basicFactoryInjection(Factory<Product> factory) {
        // Factory is automatically injected by AutoParams
        Product product = factory.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotEmpty();
        assertThat(product.getImageUri()).isNotEmpty();
        assertThat(product.getDescription()).isNotEmpty();
        assertThat(product.getPriceAmount()).isNotNull();
        assertThat(product.getStockQuantity()).isNotNegative();
    }

    @Test
    @AutoParams
    void genericFactoryInjection(Factory<List<Product>> listFactory) {
        // Generic Factory is also automatically injected
        List<Product> products = listFactory.get();

        assertThat(products).isNotNull();
        assertThat(products).hasSize(3); // Default collection size
        assertThat(products).allSatisfy(product -> {
            assertThat(product.getName()).isNotEmpty();
            assertThat(product.getPriceAmount()).isNotNull();
        });
    }

    @Test
    @AutoParams
    void multipleFactoryInjection(
        Factory<Product> productFactory,
        Factory<List<Product>> listFactory
    ) {
        // Multiple factories can be injected in the same test
        Product singleProduct = productFactory.get();
        List<Product> productList = listFactory.get();

        assertThat(singleProduct).isNotNull();
        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(3);

        // Each factory generates independent objects
        assertThat(productList).doesNotContain(singleProduct);
    }
}
