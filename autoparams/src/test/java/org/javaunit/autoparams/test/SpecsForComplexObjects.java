package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForComplexObjects {

    @ParameterizedTest
    @AutoSource
    void sut_creates_complex_object(ComplexObject actual) {
        assertNotNull(actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_complex_objects(ComplexObject value1, ComplexObject value2) {
        assertThat(value1).isNotEqualTo(value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_object_having_complex_object(MoreComplexObject actual) {
        assertNotNull(actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_objects_having_complex_object(
        MoreComplexObject value1,
        MoreComplexObject value2
    ) {
        assertThat(value1).isNotEqualTo(value2);
    }

}
