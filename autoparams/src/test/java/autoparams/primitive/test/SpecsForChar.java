package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForChar {

    @AutoParameterizedTest
    void sut_creates_arbitrary_char_values(
        char value1,
        char value2,
        char value3,
        char value4,
        char value5
    ) {
        HashSet<Character> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Character_values(
        Character value1,
        Character value2,
        Character value3,
        Character value4,
        Character value5
    ) {
        HashSet<Character> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }
}
