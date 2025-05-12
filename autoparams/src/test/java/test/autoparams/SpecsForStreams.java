package test.autoparams;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.validation.constraints.Size;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

class SpecsForStreams {

    @ParameterizedTest
    @AutoSource
    void sut_creates_IntStream(IntStream intStream) {
        assertThat(intStream).isNotNull();
        assertThat(intStream).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_applies_customizer_to_IntStream(
        ResolutionContext sut,
        int value
    ) {
        sut.applyCustomizer((query, context) ->
            query.getType().equals(int.class)
                ? new ObjectContainer(value)
                : ObjectContainer.EMPTY);

        IntStream actual = sut.resolve(IntStream.class);

        assertThat(actual).containsOnly(value);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_LongStream(LongStream longStream) {
        assertThat(longStream).isNotNull();
        assertThat(longStream).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_applies_customizer_to_LongStream(
        ResolutionContext sut,
        long value
    ) {
        sut.applyCustomizer((query, context) ->
            query.getType().equals(long.class)
                ? new ObjectContainer(value)
                : ObjectContainer.EMPTY);

        LongStream actual = sut.resolve(LongStream.class);

        assertThat(actual).containsOnly(value);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_DoubleStream(DoubleStream doubleStream) {
        assertThat(doubleStream).isNotNull();
        assertThat(doubleStream).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_applies_customizer_to_DoubleStream(
        ResolutionContext sut,
        double value
    ) {
        sut.applyCustomizer((query, context) ->
            query.getType().equals(double.class)
                ? new ObjectContainer(value)
                : ObjectContainer.EMPTY);

        DoubleStream actual = sut.resolve(DoubleStream.class);

        assertThat(actual).containsOnly(value);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_generic_stream(Stream<UUID> stream) {
        Object[] array = stream.toArray();
        Set<Object> set = stream(array).collect(Collectors.toSet());
        assertThat(array).hasSize(3);
        assertThat(set).hasSize(array.length);
    }

    @Test
    @AutoParams
    void sut_creates_generic_stream_with_elements_as_many_as_min_of_size_annotation(
        @Size(min = 5) Stream<String> stream
    ) {
        assertThat(stream).hasSize(5);
    }

    @Test
    @AutoParams
    void sut_creates_IntStream_with_elements_as_many_as_min_of_size_annotation(
        @Size(min = 5) IntStream stream
    ) {
        assertThat(stream).hasSize(5);
    }

    @Test
    @AutoParams
    void sut_creates_LongStream_with_elements_as_many_as_min_of_size_annotation(
        @Size(min = 5) LongStream stream
    ) {
        assertThat(stream).hasSize(5);
    }

    @Test
    @AutoParams
    void sut_creates_DoubleStream_with_elements_as_many_as_min_of_size_annotation(
        @Size(min = 5) DoubleStream stream
    ) {
        assertThat(stream).hasSize(5);
    }
}
