package test.autoparams.customization;

import autoparams.AutoParams;
import autoparams.customization.FreezeBy;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static autoparams.customization.Matching.EXACT_TYPE;
import static autoparams.customization.Matching.IMPLEMENTED_INTERFACES;
import static autoparams.customization.Matching.PARAMETER_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForFreezeBy {

    @Test
    @AutoParams
    void sut_freezes_value_by_exact_type(
        @FreezeBy(EXACT_TYPE) String value1,
        String value2
    ) {
        assertThat(value1).isSameAs(value2);
    }

    @Test
    @AutoParams
    void sut_freezes_value_by_implemented_interfaces(
        @FreezeBy(IMPLEMENTED_INTERFACES) String value1,
        CharSequence value2
    ) {
        assertThat(value1).isSameAs(value2);
    }

    @Test
    @AutoParams
    void sut_freezes_value_by_parameter_name(
        @FreezeBy(PARAMETER_NAME) String name,
        Product product
    ) {
        assertThat(product.name()).isEqualTo(name);
        assertThat(product.imageUri()).isNotEqualTo(name);
        assertThat(product.description()).isNotEqualTo(name);
    }
}
