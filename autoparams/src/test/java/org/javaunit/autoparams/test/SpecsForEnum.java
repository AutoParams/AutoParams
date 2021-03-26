package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForEnum {

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_enum_values(
        EnumType value1,
        EnumType value2,
        EnumType value3,
        EnumType value4,
        EnumType value5,
        EnumType value6,
        EnumType value7,
        EnumType value8,
        EnumType value9,
        EnumType value10,
        EnumType value11,
        EnumType value12,
        EnumType value13,
        EnumType value14,
        EnumType value15,
        EnumType value16,
        EnumType value17,
        EnumType value18,
        EnumType value19,
        EnumType value20
    ) {
        HashSet<EnumType> set = new HashSet<EnumType>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        set.add(value11);
        set.add(value12);
        set.add(value13);
        set.add(value14);
        set.add(value15);
        set.add(value16);
        set.add(value17);
        set.add(value18);
        set.add(value19);
        set.add(value20);
        assertThat(set).contains(EnumType.values());
    }

}
