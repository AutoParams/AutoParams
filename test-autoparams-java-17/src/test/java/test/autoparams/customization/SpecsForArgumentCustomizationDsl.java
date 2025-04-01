package test.autoparams.customization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import test.autoparams.IterableBag;

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
    void when_declaring_class_specified_freezeArgument_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument("id").in(Product.class).to(id));
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void when_declaring_class_specified_freezeArgument_does_not_set_argument_for_other_type(
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
    void freezeArgumentsOf_accepts_generic_type_correctly(
        ResolutionContext context,
        Iterable<UUID> iterable
    ) {
        Type type = new TypeReference<Iterable<UUID>>() { }.getType();

        context.applyCustomizer(freezeArgumentsOf(type).to(iterable));

        IterableBag<UUID> bag = context.resolve(new TypeReference<>() { });
        assertThat(bag.items()).isSameAs(iterable);
    }

    @Test
    @AutoParams
    void freezeArgumentsOf_accepts_type_reference_correctly(
        ResolutionContext context,
        Iterable<UUID> iterable
    ) {
        context.applyCustomizer(
            freezeArgumentsOf(new TypeReference<Iterable<UUID>>() { })
                .to(iterable)
        );
        IterableBag<UUID> bag = context.resolve(new TypeReference<>() { });
        assertThat(bag.items()).isSameAs(iterable);
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
    void freezeArgument_with_parameter_type_reference_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(
            freezeArgument(new TypeReference<UUID>() { }, "id").to(id)
        );
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
