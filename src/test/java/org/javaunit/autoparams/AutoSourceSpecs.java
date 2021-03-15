package org.javaunit.autoparams;

import java.util.*;
import java.util.stream.Stream;
import org.javaunit.autoparams.range.DoubleRange;
import org.javaunit.autoparams.range.FloatRange;
import org.javaunit.autoparams.range.IntRange;
import org.javaunit.autoparams.range.LongRange;
import org.junit.jupiter.params.ParameterizedTest;
import static org.assertj.core.api.Assertions.assertThat;

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
    void sut_correctly_creates_nested_generic_object(GenericObject<String, GenericObject<UUID, ComplexObject>> value) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(String.class);
        assertThat(value.getValue3()).isInstanceOf(GenericObject.class);
        assertThat(value.getValue3().getValue3()).isInstanceOf(ComplexObject.class);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_int_value_in_range(@IntRange(min = 10, max = 30) int num) {
        assertThat(num).isGreaterThanOrEqualTo(10);
        assertThat(num).isLessThanOrEqualTo(30);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_long_value_in_range(@LongRange(min = 10, max = 30) long num) {
        assertThat(num).isGreaterThanOrEqualTo(10);
        assertThat(num).isLessThanOrEqualTo(30);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_float_value_in_range(@FloatRange(min = 10.0F, max = 30.0F) float num) {
        assertThat(num).isGreaterThanOrEqualTo(10);
        assertThat(num).isLessThanOrEqualTo(30);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_double_value_in_range(@DoubleRange(min = 10.0D, max = 30.0D) double num) {
        assertThat(num).isGreaterThanOrEqualTo(10);
        assertThat(num).isLessThanOrEqualTo(30);
    }
}
