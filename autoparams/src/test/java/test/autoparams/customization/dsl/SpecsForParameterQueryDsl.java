package test.autoparams.customization.dsl;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEndsWith;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEndsWithIgnoreCase;
import static autoparams.customization.dsl.ParameterQueryDsl.parameterNameEqualsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForParameterQueryDsl {

    @Test
    @AutoParams
    void parameterNameEqualsIgnoreCase_fails_if_parameter_name_is_not_present(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEqualsIgnoreCase("stockQuantity"))
                .to(stockQuantity)
        );

        assertThatThrownBy(() -> context.resolve(Product.class))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @AutoParams
    void parameterNameEndsWith_fails_if_parameter_name_is_not_present(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEndsWith("Quantity"))
                .to(stockQuantity)
        );

        assertThatThrownBy(() -> context.resolve(Product.class))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @AutoParams
    void parameterNameEndsWithIgnoreCase_fails_if_parameter_name_is_not_present(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument()
                .where(parameterNameEndsWithIgnoreCase("quantity"))
                .to(stockQuantity)
        );

        assertThatThrownBy(() -> context.resolve(Product.class))
            .isInstanceOf(RuntimeException.class);
    }
}
