package test.autoparams.generator;

import autoparams.AutoParams;
import autoparams.generator.Factory;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

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
}
