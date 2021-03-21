package org.javaunit.autoparams.autofixture;

import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

class DataGeneratorTest {
    @ParameterizedTest
    @AutoData
    void generate(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h) {
        assertThat(a || b || c || d || e || f || g || h).isTrue();
    }

    @ParameterizedTest
    @AutoData
    void generate(char a, char b) {
        assertThat(a).isNotEqualTo(b);
    }

    @ParameterizedTest
    @AutoData
    void generate(int a, int b) {
        int max = Math.max(a, b);
        assertThat(abs(max)).isGreaterThan(0);
    }

    @ParameterizedTest
    @AutoData
    void generate(long a, long b) {
        long max = Math.max(a, b);
        assertThat(abs(max)).isGreaterThan(0);
    }

    @ParameterizedTest
    @AutoData
    void generate(double a, double b) {
        double max = Math.max(a, b);
        assertThat(abs(max)).isGreaterThan(0);
    }

    @ParameterizedTest
    @AutoData
    void generate(float a,float b) {
        float max = Math.max(a, b);
        assertThat(abs(max)).isGreaterThan(0);
    }

    @ParameterizedTest
    @AutoData
    void generate(String a) {
        int length = a.length();
        assertThat(length).isGreaterThan(0);
    }

    @ParameterizedTest
    @AutoData
    void generate(TestEnum a, TestEnum b, TestEnum c, TestEnum d, TestEnum e) {
        assertThat(a != b || a != c || a != d || a != e ).isTrue();
    }

    @ParameterizedTest
    @AutoData
    void generate(UUID a) {
        assertThat(a).isNotNull();
    }

    @ParameterizedTest
    @AutoData
    void generate(List<String> a) {
        assertThat(a.size()).isGreaterThan(0);
    }

    enum TestEnum {
        T1, T2, T3, T4, T5
    }
}
