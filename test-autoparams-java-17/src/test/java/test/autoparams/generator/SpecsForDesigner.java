package test.autoparams.generator;

import java.math.BigDecimal;

import autoparams.AutoParams;
import autoparams.generator.Factory;
import org.junit.jupiter.api.Test;
import test.autoparams.Order;
import test.autoparams.Product;
import test.autoparams.Review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForDesigner {

    @Test
    void design_throws_exception_when_argument_type_is_null() {
        assertThatThrownBy(() -> Factory.design(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("type");
    }

    @Test
    void sut_creates_an_object_of_the_specified_type() {
        Product actual = Factory.design(Product.class).create();
        assertThat(actual).isInstanceOf(Product.class);
    }

    @Test
    @AutoParams
    void sut_sets_property_value_when_using_method_reference(String name) {
        Product actual = Factory
            .design(Product.class)
            .set(Product::name).to(name)
            .create();

        assertThat(actual.name()).isEqualTo(name);
    }

    @Test
    @AutoParams
    void sut_overwrites_property_value_when_set_multiple_times(
        String firstName,
        String secondName
    ) {
        Product actual = Factory
            .design(Product.class)
            .set(Product::name).to(firstName)
            .set(Product::name).to(secondName)
            .create();

        assertThat(actual.name()).isEqualTo(secondName);
    }

    @Test
    void sut_throws_exception_when_property_getter_delegate_is_null() {
        assertThatThrownBy(() -> Factory.design(Product.class).set(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void sut_applies_processor_to_created_object() {
        Order actual = Factory
            .design(Order.class)
            .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
            .process(o -> o.applyPercentDiscount(BigDecimal.valueOf(10)))
            .create();

        assertThat(actual.getDiscountedPrice())
            .isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void sut_applies_multiple_processors_in_sequence() {
        Order actual = Factory
            .design(Order.class)
            .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
            .process(o -> o.applyPercentDiscount(BigDecimal.valueOf(10)))
            .process(o -> o.applyAmountDiscount(BigDecimal.valueOf(5)))
            .create();

        assertThat(actual.getDiscountedPrice())
            .isEqualByComparingTo(BigDecimal.valueOf(85));
    }

    @Test
    void sut_throws_exception_when_processor_is_null() {
        assertThatThrownBy(() -> Factory.design(Product.class).process(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @AutoParams
    void withDesign_configures_nested_object_using_design_function(
        String productName,
        String imageName,
        String comment
    ) {
        String imageUri = "http://example.com/images/" + imageName;

        Review actual = Factory
            .design(Review.class)
            .set(Review::product).withDesign(product -> product
                .set(Product::name).to(productName)
                .set(Product::imageUri).to(imageUri)
            )
            .set(Review::comment).to(comment)
            .create();

        assertThat(actual.product().name()).isEqualTo(productName);
        assertThat(actual.product().imageUri()).isEqualTo(imageUri);
        assertThat(actual.comment()).isEqualTo(comment);
    }
}
