package test.autoparams;

import autoparams.AutoParams;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForAutoParams {

    public record Container(String value1, String value2) { }

    @Test
    @AutoParams
    void sut_use_parameter_name_as_prefix(Container container) {
        assertThat(container.value1()).startsWith("value1");
        assertThat(container.value2()).startsWith("value2");
    }
}
