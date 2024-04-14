package test.autoparams;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import org.junit.jupiter.params.ParameterizedTest;

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
        Set<Object> set = Arrays.stream(array).collect(Collectors.toSet());
        assertThat(array).hasSize(3);
        assertThat(set).hasSize(array.length);
    }
}
