package autoparams;

import org.junit.jupiter.api.extension.ParameterContext;

@FunctionalInterface
interface ParameterScanBrake {

    boolean shouldBrakeBefore(ParameterContext parameterContext);
}
