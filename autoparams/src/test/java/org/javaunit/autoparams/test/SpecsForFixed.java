package org.javaunit.autoparams.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.Fixed;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForFixed {

    @ParameterizedTest
    @AutoSource
    void sut_reuses_generated_value_for_same_type(
        @Fixed String value,
        ValueContainer<String> container
    ) {
        assertEquals(value, container.getValue());
    }

    @ParameterizedTest
    @AutoSource
    void sut_reuses_generated_value_for_remaining_same_type_arguments(
        String value1,
        @Fixed String value2,
        String value3
    ) {
        assertNotEquals(value1, value2);
        assertEquals(value2, value3);
    }

}
