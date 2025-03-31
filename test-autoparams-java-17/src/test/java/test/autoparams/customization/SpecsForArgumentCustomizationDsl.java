package test.autoparams.customization;

import java.math.BigDecimal;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

import static autoparams.customization.ArgumentCustomizationDsl.freezeArgument;
import static autoparams.customization.ArgumentCustomizationDsl.freezeArgumentsOf;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentCustomizationDsl {

    public record Product(
        UUID id,
        String name,
        String imageUri,
        String description,
        BigDecimal priceAmount,
        int stockQuantity
    ) {
    }

    public record Seller(UUID id, String email, String username) {
    }

    public record Comment(long id, String content) {
    }

    @Test
    @AutoParams
    void freezeArgument_correctly_sets_argument_with_name(
        ResolutionContext context,
        int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument("stockQuantity").to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void when_type_specified_freezeArgument_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument("id").in(Product.class).to(id));
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void when_type_specified_freezeArgument_does_not_set_argument_for_other_type(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument("id").in(Product.class).to(id));
        Seller seller = context.resolve(Seller.class);
        assertThat(seller.id()).isNotEqualTo(id);
    }

    @Test
    @AutoParams
    void freezeArgumentsOf_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgumentsOf(UUID.class).to(id));
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void freezeArgument_with_parameter_type_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument(UUID.class, "id").to(id));
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void freezeArgument_with_parameter_type_does_not_set_argument_for_other_type(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument(UUID.class, "id").to(id));
        Comment comment = context.resolve(Comment.class);
        assertThat(comment.id()).isNotEqualTo(id);
    }
}
