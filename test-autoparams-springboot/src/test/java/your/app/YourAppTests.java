package your.app;

import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

public class YourAppTests {

    @Test
    void testMethodReporting() {
        ResolutionContext context = new ResolutionContext();
        User user = context.resolve();
    }
}
