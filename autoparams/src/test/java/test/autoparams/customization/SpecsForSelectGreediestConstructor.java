package test.autoparams.customization;

import java.beans.ConstructorProperties;

import autoparams.AutoParams;
import autoparams.customization.SelectGreediestConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForSelectGreediestConstructor {

    @Getter
    public static class StringContainer {

        private final String value;

        public StringContainer(String value) {
            this.value = value;
        }

        public StringContainer() {
            this(null);
        }
    }

    @Getter
    public static class StringsContainer {

        private final String value1;
        private final String value2;

        public StringsContainer(String value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @ConstructorProperties({ "value1" })
        public StringsContainer(String value1) {
            this(value1, null);
        }

        public StringsContainer() {
            this(null, null);
        }
    }

    @Test
    @AutoParams
    void sut_selects_constructor_with_most_parameters(
        @SelectGreediestConstructor StringContainer actual
    ) {
        assertThat(actual.getValue()).isNotNull();
    }

    @Test
    @AutoParams
    void sut_selects_constructor_with_ConstructorProperties_first(
        @SelectGreediestConstructor StringsContainer actual
    ) {
        assertThat(actual.getValue1()).isNotNull();
        assertThat(actual.getValue2()).isNull();
    }
}
