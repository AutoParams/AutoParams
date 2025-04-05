package test.autoparams.customization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import autoparams.AutoParams;
import autoparams.customization.ResolveConstructorAggressively;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForResolveConstructorAggressively {

    @Getter
    public static class StringContainer1 {

        private final String value;

        public StringContainer1(String value) {
            this.value = value;
        }

        public StringContainer1() {
            this(null);
        }
    }

    @Getter
    public static class StringContainer2 {

        private final String value;

        public StringContainer2(String value) {
            this.value = value;
        }

        public StringContainer2() {
            this(null);
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

    @Retention(RetentionPolicy.RUNTIME)
    @AutoParams
    @ResolveConstructorAggressively(StringContainer1.class)
    public @interface AutoParamsWithResolveConstructorAggressively {
    }

    @Test
    @AutoParamsWithResolveConstructorAggressively
    void sut_can_be_used_on_annotation(StringContainer1 actual) {
        assertThat(actual.getValue()).isNotNull();
    }

    @Test
    @AutoParams
    void sut_can_be_used_on_parameter(
        StringContainer1 container1,
        @ResolveConstructorAggressively(StringContainer1.class) StringContainer1 container2
    ) {
        assertThat(container1.getValue()).isNull();
        assertThat(container2.getValue()).isNotNull();
    }

    @Test
    @AutoParams
    @ResolveConstructorAggressively({
        StringContainer1.class,
        StringContainer2.class
    })
    void sut_provides_implementation_for_multiple_types_correctly(
        StringContainer1 container1,
        StringContainer2 container2
    ) {
        assertThat(container1.getValue()).isNotNull();
        assertThat(container2.getValue()).isNotNull();
    }
}
