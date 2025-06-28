package test.autoparams.generator;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.generator.Designer;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import test.autoparams.Category;
import test.autoparams.Order;
import test.autoparams.Product;
import test.autoparams.Review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForDesigner {

    @Test
    void design_throws_exception_when_argument_type_is_null() {
        assertThatThrownBy(() -> Designer.design(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("type");
    }

    @Test
    void sut_creates_an_object_of_the_specified_type() {
        Product actual = Designer.design(Product.class).create();
        assertThat(actual).isInstanceOf(Product.class);
    }

    @Test
    @AutoParams
    void sut_sets_property_value_when_using_method_reference(String name) {
        Product actual = Designer
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
        Product actual = Designer
            .design(Product.class)
            .set(Product::name).to(firstName)
            .set(Product::name).to(secondName)
            .create();

        assertThat(actual.name()).isEqualTo(secondName);
    }

    @Test
    void sut_throws_exception_when_property_getter_delegate_is_null() {
        assertThatThrownBy(() -> Designer.design(Product.class).set(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void sut_applies_processor_to_created_object() {
        Order actual = Designer
            .design(Order.class)
            .set(Order::getOriginalPrice).to(BigDecimal.valueOf(100))
            .process(o -> o.applyPercentDiscount(BigDecimal.valueOf(10)))
            .create();

        assertThat(actual.getDiscountedPrice())
            .isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void sut_applies_multiple_processors_in_sequence() {
        Order actual = Designer
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
        assertThatThrownBy(() -> Designer.design(Product.class).process(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @AutoParams
    void using_configures_nested_object_using_design_function(
        String productName,
        String imageName,
        String comment
    ) {
        String imageUri = "http://example.com/images/" + imageName;

        Review actual = Designer
            .design(Review.class)
            .set(Review::product).using(product -> product
                .set(Product::name).to(productName)
                .set(Product::imageUri).to(imageUri)
            )
            .set(Review::comment).to(comment)
            .create();

        assertThat(actual.product().name()).isEqualTo(productName);
        assertThat(actual.product().imageUri()).isEqualTo(imageUri);
        assertThat(actual.comment()).isEqualTo(comment);
    }

    @Test
    void using_throws_exception_when_design_function_argument_is_null() {
        assertThatThrownBy(() -> Designer
            .design(Review.class)
            .set(Review::product).using(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    public record User(UUID id, String username) { }

    public record Following(User followee, User follower) { }

    @Test
    @AutoParams
    void using_does_not_affect_properties_outside_the_nested_object(
        String followeeName
    ) {
        Following actual = Designer
            .design(Following.class)
            .set(Following::followee).using(user -> user
                .set(User::username).to(followeeName)
            )
            .create();

        assertThat(actual.followee().username()).isEqualTo(followeeName);
        assertThat(actual.follower().username()).isNotEqualTo(followeeName);
        assertThat(actual.followee()).isNotSameAs(actual.follower());
    }

    @Test
    @AutoParams
    void using_supports_multiple_levels_of_nested_object_configuration(
        String reviewComment,
        String productName,
        String categoryName
    ) {
        Review actual = Designer
            .design(Review.class)
            .set(Review::comment).to(reviewComment)
            .set(Review::product).using(product -> product
                .set(Product::name).to(productName)
                .set(Product::category).using(category -> category
                    .set(Category::name).to(categoryName)
                )
            )
            .create();

        assertThat(actual.comment()).isEqualTo(reviewComment);
        assertThat(actual.product().name()).isEqualTo(productName);
        assertThat(actual.product().category().name()).isEqualTo(categoryName);
    }

    @Test
    void create_throws_exception_when_design_function_does_not_return_its_argument() {
        Designer<Review> designer = Designer
            .design(Review.class)
            .set(Review::product).using(product -> null);
        assertThatThrownBy(designer::create)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void sut_is_resolved_from_ResolutionContext() {
        var context = new ResolutionContext();
        Designer<Product> designer = context.resolve(new TypeReference<>() { });
        assertThat(designer).isNotNull();
        assertThat(designer.create()).isInstanceOf(Product.class);
    }

    @Test
    @AutoParams
    void sut_is_injected_as_a_parameter_using_AutoParams(
        Designer<Product> designer
    ) {
        assertThat(designer).isNotNull();
        assertThat(designer.create()).isInstanceOf(Product.class);
    }

    @Test
    @AutoParams
    void sut_returns_stream_of_objects_with_configured_property_values(
        String productName
    ) {
        Stream<Product> stream = Designer
            .design(Product.class)
            .set(Product::name).to(productName)
            .stream();

        assertThat(stream.limit(5))
            .allSatisfy(p -> assertThat(p.name()).isEqualTo(productName));
    }
}
