package org.javaunit.autoparams;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;

public class AutoSourceSpecs {

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_array_of_int(int[] array) {
        assertThat(array).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_int_with_arbitrary_values(int[] array) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_Integer_with_arbitrary_values(Integer[] array) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_complex_type_with_arbitrary_objects(ComplexObject[] array) {
        HashSet<ComplexObject> set = new HashSet<ComplexObject>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource(repeat = 10)
    void sut_fills_array_of_enum_type_with_arbitrary_objects(EnumType[] array) {
        HashSet<EnumType> set = new HashSet<>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set.size()).isGreaterThanOrEqualTo(1);
        assertThat(set.size()).isLessThanOrEqualTo(EnumType.values().length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_array_list(ArrayList<ComplexObject> arrayList) {
        assertThat(arrayList).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_list(ArrayList<UUID> arrayList) {
        HashSet<UUID> set = new HashSet<UUID>();
        for (UUID x : arrayList) {
            set.add(x);
        }

        assertThat(arrayList).hasSize(3);
        assertThat(set).hasSize(arrayList.size());
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_list(List<ComplexObject> list) {
        assertThat(list).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_collection(Collection<ComplexObject> collection) {
        assertThat(collection).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_intStream(IntStream intStream) {
        assertThat(intStream).isNotNull();
        assertThat(intStream).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_iterable(Iterable<ComplexObject> iterable) {
        assertThat(iterable).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_stream(Stream<UUID> stream) {
        assertThat(stream).isNotNull();

        Object[] array = stream.toArray();
        HashSet<UUID> set = new HashSet<UUID>();
        for (int i = 0; i < array.length; i++) {
            set.add((UUID) array[i]);
        }

        assertThat(array).hasSize(3);
        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_hash_map(HashMap<Integer, String> map) {
        assertThat(map).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_fills_hash_map(HashMap<Integer, String> map) {
        assertThat(map).hasSize(3);
        HashSet<String> set = new HashSet<String>(map.values());
        assertThat(set).hasSize(map.keySet().size());
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_map(Map<Integer, String> map) {
        assertThat(map).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_hash_set(HashSet<String> set) {
        assertThat(set).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_fills_hash_set(HashSet<String> set) {
        assertThat(set).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_set(Set<String> set) {
        assertThat(set).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_generic_object(GenericObject<UUID, ComplexObject> value) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(UUID.class);
        assertThat(value.getValue3()).isInstanceOf(ComplexObject.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_nested_generic_object(
        GenericObject<String, GenericObject<UUID, ComplexObject>> value) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(String.class);
        assertThat(value.getValue3()).isInstanceOf(GenericObject.class);
        assertThat(value.getValue3().getValue3()).isInstanceOf(ComplexObject.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_selects_constructor_with_the_fewest_parameters(HasMultipleConstructors container) {
        assertThat(container.getValue()).isNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_selects_constructor_decorated_with_ConstructorProperties(
        DecoratedWithConstructorProperties container) {

        assertThat(container.getValue()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_char_value(char character) {
        assertThat(character).isBetween(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_Character_value(Character character) {
        assertThat(character).isInstanceOf(Character.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_BigInteger_value(BigInteger bigInteger) {
        assertThat(bigInteger).isInstanceOf(BigInteger.class);
    }
}
