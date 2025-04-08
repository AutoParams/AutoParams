package test.autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import test.autoparams.Comment;
import test.autoparams.IterableBag;
import test.autoparams.Product;
import test.autoparams.Seller;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgumentOf;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForArgumentCustomizationDslUsingRecords {

    @Test
    @AutoParams
    void freezeArgument_correctly_sets_argument_with_name(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument("stockQuantity").to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void freezeArgumentOf_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgumentOf(UUID.class).to(id));
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void freezeArgumentOf_accepts_generic_type_correctly(
        ResolutionContext context,
        Iterable<UUID> iterable
    ) {
        Type type = new TypeReference<Iterable<UUID>>() { }.getType();

        context.applyCustomizer(freezeArgumentOf(type).to(iterable));

        IterableBag<UUID> bag = context.resolve(new TypeReference<>() { });
        assertThat(bag.items()).isSameAs(iterable);
    }

    @Test
    @AutoParams
    void freezeArgumentOf_accepts_type_reference_correctly(
        ResolutionContext context,
        Iterable<UUID> iterable
    ) {
        context.applyCustomizer(
            freezeArgumentOf(new TypeReference<Iterable<UUID>>() { })
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
    void freezeArgument_with_parameter_type_does_not_set_argument_for_other_type(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument(UUID.class, "id").to(id));
        Comment comment = context.resolve(Comment.class);
        assertThat(comment.id()).isNotEqualTo(id);
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
    void freezeArgument_with_predicate_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(
            freezeArgument(query -> query
                .getRequiredParameterName()
                .equals("id")
            ).to(id)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void freezeArgument_with_no_parameters_correctly_sets_argument(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(freezeArgument().to(null));
        Seller seller = context.resolve(Seller.class);
        assertThat(seller.id()).isNull();
        assertThat(seller.email()).isNull();
        assertThat(seller.username()).isNull();
    }
}
