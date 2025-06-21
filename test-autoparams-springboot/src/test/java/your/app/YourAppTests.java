package your.app;

import autoparams.AutoParams;
import autoparams.LogResolution;
import org.junit.jupiter.api.Test;

public class YourAppTests {

    @Test
    @AutoParams
    @LogResolution
    void testMethodReporting(User user) {
    }
}
