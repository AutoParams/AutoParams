package examples.basic;

import autoparams.AutoParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple test to verify AutoParams installation is working correctly.
 * This test corresponds to the example in the Installation Guide.
 */
class InstallationTest {
    
    @Test
    @AutoParams
    void autoParamsWorking(String value) {
        assertNotNull(value);
    }
}
