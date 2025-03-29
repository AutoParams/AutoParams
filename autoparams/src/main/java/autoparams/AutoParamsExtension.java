package autoparams;

import java.lang.reflect.Parameter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public final class AutoParamsExtension implements
    BeforeTestExecutionCallback,
    ParameterResolver {

    private TestResolutionContext resolutionContext = null;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        resolutionContext = TestResolutionContext.create(context);
    }

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return TestGear.TYPES.contains(parameter.getType()) == false;
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        TestParameterContext testParameterContext = new TestParameterContext(
            resolutionContext,
            new ParameterQuery(
                parameterContext.getParameter(),
                parameterContext.getIndex(),
                parameterContext.getParameter().getParameterizedType()
            )
        );
        return testParameterContext.resolveArgument();
    }
}
