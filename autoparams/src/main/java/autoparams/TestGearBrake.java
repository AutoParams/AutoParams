package autoparams;

import java.lang.reflect.Parameter;
import java.util.List;

final class TestGearBrake implements Brake {

    private static final List<Class<?>> GEARS = TestGear.TYPES;

    public static final TestGearBrake INSTANCE = new TestGearBrake();

    private TestGearBrake() {
    }

    @Override
    public boolean shouldBrakeBefore(Parameter parameter) {
        return GEARS.contains(parameter.getType());
    }
}
