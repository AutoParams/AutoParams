package examples.basics.design;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import autoparams.customization.Design;
import examples.Product;
import examples.Review;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DesignBasicExamples {

    @Test
    void propertyConfigurationWithFixedValues() {
        // Property Configuration with Fixed Values example
        Product product = Design.of(Product.class)
            .set(Product::getName, "Custom Product Name")
            .set(Product::getPrice, 99.99)
            .instantiate();

        assertEquals("Custom Product Name", product.getName());
        assertEquals(99.99, product.getPrice());
    }

    @Test
    void propertyConfigurationWithDynamicValues() {
        // Property Configuration with Dynamic Values example
        AtomicInteger counter = new AtomicInteger(0);

        Design<Product> design = Design.of(Product.class)
            .supply(Product::getName, () -> "Product " + counter.incrementAndGet())
            .supply(Product::getPrice, () -> Math.random() * 100);

        Product product1 = design.instantiate();
        Product product2 = design.instantiate();

        assertEquals("Product 1", product1.getName());
        assertEquals("Product 2", product2.getName());
        // Prices will be different random values
    }

    @Test
    void nestedObjectConfiguration() {
        // Nested Object Configuration example
        Random random = new Random();

        Review review = Design.of(Review.class)
            .set(Review::getRating, 5)
            .design(Review::getProduct, product -> product
                .set(Product::getName, "Amazing Widget")
                .supply(Product::getPrice, () -> random.nextInt(10, 100)))
            .instantiate();

        // Verify configured properties
        assertEquals(5, review.getRating());
        Product product = review.getProduct();
        assertEquals("Amazing Widget", product.getName());
        assertTrue(product.getPrice() >= 10 && product.getPrice() < 100);
    }
}
