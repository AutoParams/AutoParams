package org.javaunit.autoparams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;

public class AutoSourceSpecs {

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
    void sut_creates_longStream(LongStream longStream) {
        assertThat(longStream).isNotNull();
        assertThat(longStream).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_doubleStream(DoubleStream doubleStream) {
        assertThat(doubleStream).isNotNull();
        assertThat(doubleStream).hasSize(3);
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
    void sut_creates_builder_generate_integer_array(Builder<Integer[]> builder) {
        Integer[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_integer_list(Builder<List<Integer>> builder) {
        List<Integer> value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_complex_type_array(Builder<ComplexObject[]> builder) {
        ComplexObject[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_complex_type_map(
        Builder<Map<String, ComplexObject>> builder
    ) {
        Map<String, ComplexObject> value = builder.build();
        assertThat(value).isNotEmpty();
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_interface_is_requested(
        Builder<Cloneable> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("interface");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_is_requested(
        Builder<AbstractList<Object>> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_with_public_constructor_is_requested(
        Builder<AbstractWithPublicConstructor> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("abstract");
    }

}
