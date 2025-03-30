package autoparams;

import java.lang.reflect.Parameter;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

class TestGearBlock implements SupportedParameterPredicate {

    @Override
    public boolean test(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) {
        Parameter parameter = parameterContext.getParameter();
        return TestGear.TYPES.contains(parameter.getType()) == false;
    }
}
