package test.autoparams;

import autoparams.AutoSource;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForStreams {

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
    void sut_creates_generic_stream(Stream<UUID> stream) {
        assertThat(stream).isNotNull();

        Object[] array = stream.toArray();
        HashSet<UUID> set = new HashSet<>();
        for (Object element : array) {
            set.add((UUID) element);
        }

        assertThat(array).hasSize(3);
        assertThat(set).hasSize(array.length);
    }

}
