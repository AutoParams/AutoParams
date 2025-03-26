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

    private ResolutionContext resolutionContext = null;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        resolutionContext = new ResolutionContext(context);
    }

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        return true;
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        ParameterQuery query = new ParameterQuery(
            parameter,
            parameterContext.getIndex(),
            parameter.getParameterizedType()
        );
        return resolutionContext.resolve(query);
    }
}
