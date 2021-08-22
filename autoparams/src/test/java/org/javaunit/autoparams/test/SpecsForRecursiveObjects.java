package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForRecursiveObjects {

    @ParameterizedTest
    @AutoSource
    void sut_creates_recursive_object(RecursiveObject actual) {
        assertNotNull(actual);
        assertNotNull(actual.getValue4().get(0));
        assertNotNull(actual.getValue4().get(0).getValue4().get(0));
        assertNull(actual.getValue4().get(0).getValue4().get(0).getValue4().get(0));
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_recursive_objects(RecursiveObject value1, RecursiveObject value2) {
        assertThat(value1).isNotEqualTo(value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_object_having_recursive_object(ComplexRecursiveObject actual) {
        assertNotNull(actual);
        assertNotNull(actual.getValue4().get(0));
        assertNotNull(actual.getValue4().get(0).getValue4().get(0));
        assertNull(actual.getValue4().get(0).getValue4().get(0).getValue4().get(0));

        assertNotNull(actual.getValue5().get(0));
        assertNotNull(actual.getValue5().get(0).getValue4().get(0));
        assertNotNull(actual.getValue5().get(0).getValue4().get(0).getValue4().get(0));
        assertNull(
            actual.getValue5().get(0).getValue4().get(0).getValue4().get(0).getValue4().get(0)
        );
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_objects_having_recursive_object(
        ComplexRecursiveObject value1,
        ComplexRecursiveObject value2
    ) {
        assertThat(value1).isNotEqualTo(value2);
    }

}
