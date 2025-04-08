package test.autoparams.customization.dsl;

import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.ValueAutoSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.Product;
import test.autoparams.Seller;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static autoparams.customization.dsl.ParameterQueryDsl.declaringClassEquals;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEndsWith;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEndsWithIgnoreCase;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEquals;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEqualsIgnoreCase;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterTypeMatches;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForParameterQueryDslUsingRecords {

    @Test
    @AutoParams
    void parameterNameEquals_creates_predicate_correctly(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEquals("stockQuantity"))
                .to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @ParameterizedTest
    @ValueAutoSource(strings = { "stockQuantity", "StockQuantity" })
    void parameterNameEqualsIgnoreCase_creates_predicate_correctly(
        String parameterName,
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEqualsIgnoreCase(parameterName))
                .to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @ParameterizedTest
    @ValueAutoSource(strings = { "Quantity", "y" })
    void parameterNameEndsWith_creates_predicate_correctly(
        String suffix,
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument(parameterNameEndsWith(suffix)).to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @ParameterizedTest
    @ValueAutoSource(strings = { "Quantity", "quantity" })
    void parameterNameEndsWithIgnoreCase_creates_predicate_correctly(
        String suffix,
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEndsWithIgnoreCase(suffix))
                .to(stockQuantity)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void parameterTypeMatches_creates_predicate_correctly(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterTypeMatches(UUID.class))
                .to(id)
        );
        Product product = context.resolve(Product.class);
        assertThat(product.id()).isEqualTo(id);
    }

    @Test
    @AutoParams
    void declaringClassEquals_creates_predicate_correctly(
        ResolutionContext context,
        UUID id
    ) {
        context.applyCustomizer(
            freezeArgument("id")
                .where(declaringClassEquals(Product.class))
                .to(id)
        );
        Product product = context.resolve(Product.class);
        Seller seller = context.resolve(Seller.class);
        assertThat(product.id()).isEqualTo(id);
        assertThat(seller.id()).isNotEqualTo(id);
    }
}
