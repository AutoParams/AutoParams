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
    void sut_fixed_only_effective_with_the_parameters_located_behind(
        String value,
        @Fixed ValueContainer<Integer> container
    ) {
        assertNotEquals(value, container.getValue());
    }

}
