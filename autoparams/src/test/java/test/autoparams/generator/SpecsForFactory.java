package test.autoparams.generator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.ValueAutoSource;
import autoparams.generator.Factory;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.ComplexObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForFactory {

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_integer_array(Factory<Integer[]> sut) {
        Integer[] value = sut.get();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_integer_list(Factory<List<Integer>> sut) {
        List<Integer> value = sut.get();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_array_of_complex_type(
        Factory<ComplexObject[]> sut
    ) {
        ComplexObject[] value = sut.get();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_map(
        Factory<Map<String, ComplexObject>> sut
    ) {
        Map<String, ComplexObject> value = sut.get();
        assertThat(value).isNotEmpty();
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 10, 20, 30 })
    void stream_returns_stream_of_anonymous_objects(
        int size,
        Factory<UUID> sut
    ) {
        Stream<UUID> stream = sut.stream();
        Set<UUID> set = stream.limit(size).collect(Collectors.toSet());
        assertThat(set).hasSize(size);
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 10, 20, 30 })
    void getRange_returns_list_of_anonymous_objects(
        int size,
        Factory<UUID> sut
    ) {
        List<UUID> list = sut.getRange(size);
        assertThat(list).hasSize(size);
    }

    @SuppressWarnings("DataFlowIssue")
    @ParameterizedTest
    @AutoSource
    void getRange_returns_unmodifiable_list(Factory<Integer> sut) {
        List<Integer> list = sut.getRange(3);
        assertThatThrownBy(() -> list.add(1))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @ParameterizedTest
    @AutoSource
    void applyCustomizer_correctly_works(Factory<UUID> sut, UUID fixedValue) {
        sut.applyCustomizer((ObjectGenerator) (query, context) ->
            query.getType() == UUID.class
                ? new ObjectContainer(fixedValue)
                : ObjectContainer.EMPTY);

        UUID actual = sut.get();

        assertThat(actual).isEqualTo(fixedValue);
    }

    @ParameterizedTest
    @AutoSource
    void applyCustomizer_does_not_effect_main_context(
        Factory<UUID> sut,
        UUID fixedValue,
        ResolutionContext mainContext
    ) {
        sut.applyCustomizer((ObjectGenerator) (query, context) ->
            query.getType() == UUID.class
                ? new ObjectContainer(fixedValue)
                : ObjectContainer.EMPTY);

        UUID actual = mainContext.resolve(UUID.class);

        assertThat(actual).isNotEqualTo(fixedValue);
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 0, 1, 2, 5, 10 })
    void sut_returns_list_with_specified_count(int count, Factory<UUID> sut) {
        List<UUID> actual = sut.get(count);

        assertThat(actual).hasSize(count);
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { -1, -5, -10 })
    void sut_throws_exception_when_count_is_negative(int count, Factory<UUID> sut) {
        assertThatThrownBy(() -> sut.get(count))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The argument 'count' must not be less than 0.");
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 1, 2, 5, 10 })
    void sut_returns_list_with_unique_instances(int count, Factory<UUID> sut) {
        List<UUID> actual = sut.get(count);

        assertThat(actual).doesNotHaveDuplicates();
    }

    @ParameterizedTest
    @ValueAutoSource(ints = { 0, 1, 2, 5 })
    void sut_returns_immutable_list(int count, Factory<UUID> sut) {
        List<UUID> actual = sut.get(count);

        assertThatThrownBy(() -> actual.add(UUID.randomUUID()))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
