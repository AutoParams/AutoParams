package test.autoparams.customization;

import autoparams.AutoParams;
import autoparams.customization.FreezeBy;
import autoparams.customization.WriteInstanceFields;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static autoparams.customization.Matching.EXACT_TYPE;
import static autoparams.customization.Matching.FIELD_NAME;
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

    @Getter
    @Setter
    public static class StringsContainer {

        private String value1;
        private String value2;
    }

    @Test
    @AutoParams
    @WriteInstanceFields(StringsContainer.class)
    void sut_freezes_value_by_field_name(
        @FreezeBy(FIELD_NAME) String value1,
        StringsContainer container
    ) {
        assertThat(container.getValue1()).isEqualTo(value1);
        assertThat(container.getValue2()).isNotEqualTo(value1);
    }
}
