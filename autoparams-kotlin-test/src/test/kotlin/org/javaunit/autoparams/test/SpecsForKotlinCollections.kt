package org.javaunit.autoparams.test

import org.assertj.core.api.Assertions.assertThat
import org.javaunit.autoparams.AutoSource
import org.junit.jupiter.params.ParameterizedTest

internal class SpecsForKotlinCollections {

    @ParameterizedTest
    @AutoSource
    internal fun sut_creates_array_list(arrayList: ArrayList<ComplexObject>) {
        assertThat(arrayList).isNotNull
    }

    @ParameterizedTest
    @AutoSource
    internal fun sut_creates_list(list: List<ComplexObject>) {
        assertThat(list).isNotNull
    }

}
