package autoparams;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ParameterContext;

final class TestGearBrake implements Brake {

    private static final List<Class<?>> GEARS = Arrays.asList(
        TestInfo.class,
        TestReporter.class
    );

    public static final TestGearBrake INSTANCE = new TestGearBrake();

    private TestGearBrake() {
    }

    @Override
    public boolean shouldBrakeBefore(ParameterContext parameterContext) {
        return GEARS.contains(parameterContext.getParameter().getType());
    }
}
