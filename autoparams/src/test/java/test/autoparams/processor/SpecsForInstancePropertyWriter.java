package test.autoparams.processor;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.InstancePropertyWriter;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.HasSetter;

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

    public static class Product {

        private String name;
        private Double price;

        public String getName() {
            return name;
        }

        public Product setName(String name) {
            this.name = name;
            return this;
        }

        public Double getPrice() {
            return price;
        }

        public Product setPrice(Double price) {
            this.price = price;
            return this;
        }
    }
}
