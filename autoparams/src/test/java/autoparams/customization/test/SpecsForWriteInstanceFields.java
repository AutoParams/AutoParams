package autoparams.customization.test;

import autoparams.AutoParameterizedTest;
import autoparams.customization.WriteInstanceFields;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForWriteInstanceFields {

    @AutoParameterizedTest
    @WriteInstanceFields(Versioned.class)
    void sut_sets_private_fields(Versioned actual) {
        assertNotNull(actual.getVersion());
    }
}
