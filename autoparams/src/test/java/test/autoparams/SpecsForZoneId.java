package test.autoparams;

import java.time.ZoneId;

import autoparams.AutoParams;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForZoneId {

    @Test
    @AutoParams
    void sut_creates_system_default_ZoneId_value(
        ZoneId value1,
        ZoneId value2,
        ZoneId value3
    ) {
        assertThat(value1).isEqualTo(ZoneId.systemDefault());
        assertThat(value2).isEqualTo(ZoneId.systemDefault());
        assertThat(value3).isEqualTo(ZoneId.systemDefault());
    }
}
