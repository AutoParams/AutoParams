package test.autoparams.processor;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.InstancePropertyWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.ComplexInheritanceDerived;
import test.autoparams.DerivedClassWithInheritedSetter;
import test.autoparams.HasSetter;
import test.autoparams.InheritanceVisibilityTest;
import test.autoparams.InheritanceWithOverride;
import test.autoparams.TrueInheritanceIssue;

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

    @ParameterizedTest
    @AutoSource
    void sut_sets_complex_inheritance_properties(
        InstancePropertyWriter sut,
        ComplexInheritanceDerived bag,
        ResolutionContext context
    ) {
        sut.process(new DefaultObjectQuery(ComplexInheritanceDerived.class), bag, context);
        assertThat(bag.getBaseProperty()).isNotNull();
        assertThat(bag.getMiddleProperty()).isNotNull();
        assertThat(bag.getDerivedProperty()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_calls_overridden_setter_in_derived_class(
        InstancePropertyWriter sut,
        InheritanceWithOverride.DerivedWithOverriddenSetter bag,
        ResolutionContext context
    ) {
        sut.process(
            new DefaultObjectQuery(InheritanceWithOverride.DerivedWithOverriddenSetter.class),
            bag,
            context
        );
        assertThat(bag.getValue()).isNotNull();
        assertThat(bag.isSetterCalled()).isTrue(); // Base setter should be called
        assertThat(bag.isDerivedSetterCalled()).isTrue(); // Overridden setter should be called
    }

    @ParameterizedTest
    @AutoSource
    void sut_sets_overridden_protected_setter(
        InstancePropertyWriter sut,
        InheritanceVisibilityTest.DerivedFromProtectedSetter bag,
        ResolutionContext context
    ) {
        sut.process(
            new DefaultObjectQuery(InheritanceVisibilityTest.DerivedFromProtectedSetter.class),
            bag,
            context
        );
        assertThat(bag.getDerivedValue()).isNotNull(); // Public setter should work
        // Overridden public setter should be called
        assertThat(bag.getProtectedValue()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_sets_inherited_protected_setter(
        InstancePropertyWriter sut,
        TrueInheritanceIssue.DerivedWithInheritedProtectedSetter bag,
        ResolutionContext context
    ) {
        sut.process(
            new DefaultObjectQuery(TrueInheritanceIssue.DerivedWithInheritedProtectedSetter.class),
            bag,
            context
        );
        assertThat(bag.getDerivedValue()).isNotNull(); // Public setter should work
        // Protected setter from base class should NOW be called
        assertThat(bag.getProtectedValue()).isNotNull();
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Product {

        private String name;
        private Double price;
    }
}
