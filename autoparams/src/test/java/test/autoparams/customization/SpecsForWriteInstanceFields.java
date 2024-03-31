package test.autoparams.customization;

import autoparams.customization.WriteInstanceFields;
import test.autoparams.AutoParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForWriteInstanceFields {

    @AutoParameterizedTest
    @WriteInstanceFields(Versioned.class)
    void sut_sets_private_fields(Versioned actual) {
        assertNotNull(actual.getVersion());
    }

    @AutoParameterizedTest
    @WriteInstanceFields({ Versioned.class, User.class })
    void sut_correctly_works_with_multiple_types(
        Versioned versioned,
        User user
    ) {
        assertNotNull(versioned.getVersion());
        assertNotNull(user.getUsername());
    }
}
