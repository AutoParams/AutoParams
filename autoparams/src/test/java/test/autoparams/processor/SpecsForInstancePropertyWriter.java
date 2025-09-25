package test.autoparams.processor;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.InstancePropertyWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.HasSetter;
import test.autoparams.DerivedClassWithInheritedSetter;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForInstancePropertyWriter {

    @ParameterizedTest
    @AutoSource
    void sut_sets_property(
        InstancePropertyWriter sut,
        HasSetter bag,
        ResolutionContext context
    ) {
        sut.process(new DefaultObjectQuery(HasSetter.class), bag, context);
        assertThat(bag.getValue()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_sets_property_value_using_chaining_setter(
        InstancePropertyWriter sut,
        Product bag,
        ResolutionContext context
    ) {
        sut.process(new DefaultObjectQuery(Product.class), bag, context);
        assertThat(bag.getName()).isNotNull();
        assertThat(bag.getPrice()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_sets_inherited_property(
        InstancePropertyWriter sut,
        DerivedClassWithInheritedSetter bag,
        ResolutionContext context
    ) {
        sut.process(new DefaultObjectQuery(DerivedClassWithInheritedSetter.class), bag, context);
        assertThat(bag.getBaseValue()).isNotNull();
        assertThat(bag.getDerivedValue()).isNotNull();
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Product {

        private String name;
        private Double price;
    }
}
