package org.javaunit.autoparams.mockito.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.IntSupplier;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.mockito.MockitoCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForMockitoCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_correctly_creates_value_for_argument_of_interface(IntSupplier value) {
        assertNotNull(value);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_yields_for_concrete_type(ConcreteClass value) {
        assertEquals(ConcreteClass.class, value.getClass());
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_correctly_creates_value_for_argument_of_abstract_class(AbstractClass value) {
        assertNotNull(value);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void sut_supports_generic_interface(GenericInterface<Integer> value) {
        assertNotNull(value);
    }

}
