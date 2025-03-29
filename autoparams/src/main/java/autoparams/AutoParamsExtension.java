package autoparams;

import java.lang.reflect.Parameter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public final class AutoParamsExtension implements
    BeforeTestExecutionCallback,
    ParameterResolver {

    private static final Namespace NAMESPACE;

    static {
        NAMESPACE = Namespace.create(AutoParamsExtension.class);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Store store = context.getStore(NAMESPACE);
        store.getOrComputeIfAbsent(
            context,
            TestResolutionContext::create,
            TestResolutionContext.class
        );
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

        Store store = extensionContext.getStore(NAMESPACE);
        TestResolutionContext resolutionContext = store.get(
            extensionContext,
            TestResolutionContext.class
        );
        Parameter parameter = parameterContext.getParameter();
        int index = parameterContext.getIndex();
        return resolutionContext.resolveArgument(parameter, index);
    }
}
