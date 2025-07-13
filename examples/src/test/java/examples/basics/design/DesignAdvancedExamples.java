package examples.basics.design;

import java.util.List;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.customization.CompositeCustomizer;
import autoparams.customization.Customization;
import autoparams.customization.Design;
import examples.Product;
import examples.Review;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DesignAdvancedExamples {

    @Test
    void resolutionContextIntegration() {
        // ResolutionContext integration example
        ResolutionContext context = new ResolutionContext();
        context.customize(set(Product::getPrice).to(30));

        Review review = Design.of(Review.class)
            .set(Review::getRating, 5)
            .instantiate(context);

        // Verify configured properties
        assertEquals(5, review.getRating());
        assertEquals(30, review.getProduct().getPrice());
    }

    @Test
    void creatinReusableCustomizers() {
        // Creating reusable Customizers example
        Design<Product> productCustomizer = Design.of(Product.class)
            .set(Product::getName, "Premium Product")
            .set(Product::getPrice, 199.99);

        ResolutionContext context = new ResolutionContext();
        context.customize(productCustomizer);

        // Design can be used as a Customizer
        Review review = context.resolve(Review.class);

        // Verify customization applied
        assertEquals("Premium Product", review.getProduct().getName());
        assertEquals(199.99, review.getProduct().getPrice());
        assertNotNull(review.getReviewer());
        assertNotNull(review.getComment());
    }

    public static class ProductCustomizer extends CompositeCustomizer {
        public ProductCustomizer() {
            super(
                Design.of(Product.class)
                    .set(Product::getName, "Premium Product")
                    .set(Product::getPrice, 199.99)
            );
        }
    }

    @Test
    @AutoParams
    @Customization(ProductCustomizer.class)
    void reusableCustomizerWithAnnotation(Review review) {
        // Review should have customized Product
        assertEquals("Premium Product", review.getProduct().getName());
        assertEquals(199.99, review.getProduct().getPrice());
        assertNotNull(review.getReviewer());
        assertNotNull(review.getComment());
    }

    @Test
    void multipleInstanceCreation() {
        // Multiple instance creation example
        Design<Product> design = Design.of(Product.class)
            .set(Product::getPrice, 25.99)
            .set(Product::getStock, 100);

        List<Product> products = design.instantiate(3);

        // Verify all instances have same configuration
        assertEquals(3, products.size());
        for (Product product : products) {
            assertEquals(25.99, product.getPrice());
            assertEquals(100, product.getStock());
        }
    }
}
