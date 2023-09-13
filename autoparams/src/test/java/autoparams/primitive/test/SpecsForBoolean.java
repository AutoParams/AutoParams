package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForBoolean {

    @AutoParameterizedTest
    void sut_creates_arbitrary_boolean_values(
        boolean value1,
        boolean value2,
        boolean value3,
        boolean value4,
        boolean value5,
        boolean value6,
        boolean value7,
        boolean value8,
        boolean value9,
        boolean value10,
        boolean value11,
        boolean value12,
        boolean value13,
        boolean value14,
        boolean value15,
        boolean value16,
        boolean value17,
        boolean value18,
        boolean value19,
        boolean value20
    ) {
        HashSet<Boolean> set = new HashSet<>();
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
        assertThat(set).hasSize(2);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Boolean_values(
        Boolean value1,
        Boolean value2,
        Boolean value3,
        Boolean value4,
        Boolean value5,
        Boolean value6,
        Boolean value7,
        Boolean value8,
        Boolean value9,
        Boolean value10,
        Boolean value11,
        Boolean value12,
        Boolean value13,
        Boolean value14,
        Boolean value15,
        Boolean value16,
        Boolean value17,
        Boolean value18,
        Boolean value19,
        Boolean value20
    ) {
        HashSet<Boolean> set = new HashSet<>();
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
        assertThat(set).hasSize(2);
    }
}
