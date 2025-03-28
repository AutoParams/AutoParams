package autoparams;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

class TestGear {

    private TestGear() {
    }

    public static final List<Class<?>> TYPES = Arrays.asList(
        TestInfo.class,
        TestReporter.class
    );
}
