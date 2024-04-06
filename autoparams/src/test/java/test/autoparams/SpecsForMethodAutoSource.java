package test.autoparams;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import autoparams.AutoSource;
import autoparams.MethodAutoSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SpecsForMethodAutoSource {

    @ParameterizedTest
    @MethodAutoSource("stringIntAndListProvider")
    void sut_correctly_fills_arguments(String str, int num, List<String> list) {
        assertThat(str).isEqualTo("apple");
        assertThat(num).isEqualTo(1);
        assertThat(list).containsSequence("a", "b");
    }

    @ParameterizedTest
    @MethodAutoSource("stringIntAndListProvider")
    void sut_generates_remaining_arguments(
        String str,
        int num,
        List<String> list,
        UUID uuid
    ) {
        assertThat(uuid).isNotNull();
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
            arguments("apple", 1, Arrays.asList("a", "b"))
        );
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_creates_instance(String[] value) {
        MethodAutoSource actual = MethodAutoSource.ProxyFactory.create(value);
        assertThat(actual).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_correctly_configures_annotationType(String[] value) {
        MethodAutoSource actual = MethodAutoSource.ProxyFactory.create(value);
        assertThat(actual.annotationType()).isEqualTo(MethodAutoSource.class);
    }

    @ParameterizedTest
    @AutoSource
    void proxy_factory_correctly_configures_properties(String[] value) {
        MethodAutoSource actual = MethodAutoSource.ProxyFactory.create(value);
        assertThat(actual.value()).isEqualTo(value);
    }
}
