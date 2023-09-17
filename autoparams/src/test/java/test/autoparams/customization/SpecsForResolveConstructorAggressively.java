package test.autoparams.customization;

import autoparams.customization.ResolveConstructorAggressively;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForResolveConstructorAggressively {

    public static class StringContainer1 {

        private final String value;

        public StringContainer1(String value) {
            this.value = value;
        }

        public StringContainer1() {
            this(null);
        }

        public String getValue() {
            return value;
        }
    }

    public static class StringContainer2 {

        private final String value;

        public StringContainer2(String value) {
            this.value = value;
        }

        public StringContainer2() {
            this(null);
        }

        public String getValue() {
            return value;
        }
    }

    @AutoParameterizedTest
    @ResolveConstructorAggressively(StringContainer1.class)
    void sut_select_constructor_with_most_parameters(StringContainer1 actual) {
        assertThat(actual.getValue()).isNotNull();
    }

    @AutoParameterizedTest
    @ResolveConstructorAggressively(StringContainer1.class)
    void sut_applies_strategy_to_only_specified_type(StringContainer2 actual) {
        assertThat(actual.getValue()).isNull();
    }
}
