package autoparams;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ParameterContext;

final class DefaultParameterScanBrake implements ParameterScanBrake {

    private static final List<Class<?>> TEST_GEARS = Arrays.asList(
        TestInfo.class,
        TestReporter.class
    );

    public static final DefaultParameterScanBrake INSTANCE;

    static {
        INSTANCE = new DefaultParameterScanBrake();
    }

    @Override
    public boolean shouldBrakeBefore(ParameterContext parameterContext) {
        return TEST_GEARS.contains(parameterContext.getParameter().getType());
    }
}
