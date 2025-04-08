package test.autoparams.customization.dsl;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForArgumentCustomizationDsl {

    @Test
    @AutoParams
    void freezeArgument_fails_if_parameter_name_is_not_present(
        ResolutionContext context,
        @Min(0) @Max(1000) int stockQuantity
    ) {
        context.applyCustomizer(
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        assertThatThrownBy(() -> context.resolve(Product.class))
            .isInstanceOf(RuntimeException.class);
    }
}
