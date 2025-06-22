package test.autoparams.generator;

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
}
