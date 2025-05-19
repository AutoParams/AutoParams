package autoparams;

import java.lang.reflect.Parameter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit 5 extension that enables automatic parameter injection for test methods
 * annotated with {@link AutoParams}.
 * <p>
 * When a test method is annotated with {@link AutoParams}, this extension
 * automatically generates and injects suitable argument values for the method's
 * parameters. This allows developers to write concise and expressive tests
 * without manual setup of parameter values.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <p>
 * Automatically injects arguments into a test method using {@link AutoParams}.
 * </p>
 * <pre>
 * // The extension is applied via the &#64;AutoParams annotation
 * &#64;Test
 * &#64;AutoParams
 * void testMethod(String arg1, UUID arg2) {
 *     // arg1 and arg2 are automatically generated and injected.
 * }
 * </pre>
 *
 * <p>
 * This extension integrates with JUnit Jupiter's extension model and supports
 * custom parameter resolution strategies for advanced scenarios.
 * </p>
 *
 * @see AutoParams
 */
public final class AutoParamsExtension implements
    BeforeTestExecutionCallback,
    ParameterResolver {

    private static final Namespace NAMESPACE;

    static {
        NAMESPACE = Namespace.create(AutoParamsExtension.class);
    }

    /**
     * Initializes the {@link ResolutionContext} before test execution.
     * <p>
     * This method is called by the JUnit 5 lifecycle before each test method
     * execution.
     * </p>
     *
     * @param context the current extension context provided by JUnit
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Store store = context.getStore(NAMESPACE);
        store.getOrComputeIfAbsent(
            context,
            TestResolutionContext::create,
            TestResolutionContext.class
        );
    }

    /**
     * Determines whether a parameter is eligible for automatic injection.
     * <p>
     * JUnit 5 calls this method to check if this extension can resolve a test
     * method parameter. It returns {@code true} if the parameter is supported
     * for automatic generation and injection, otherwise {@code false}.
     * </p>
     * <p>
     * The decision is delegated to the {@link SupportedParameterPredicate}
     * provided by the {@link ResolutionContext}. This allows for flexible and
     * customizable parameter resolution strategies.
     * </p>
     *
     * @param parameterContext the context for the parameter to be resolved
     * @param extensionContext the current extension context provided by JUnit
     * @return {@code true} if the parameter can be resolved, otherwise
     *         {@code false}
     * @throws ParameterResolutionException if an error occurs during decision
     * @see AutoParams
     * @see SupportedParameterPredicate
     */
    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext,
        ExtensionContext extensionContext
    ) throws ParameterResolutionException {

        Store store = extensionContext.getStore(NAMESPACE);
        TestResolutionContext resolutionContext = store.get(
            extensionContext,
            TestResolutionContext.class
        );
        SupportedParameterPredicate predicate = resolutionContext.resolve(
            SupportedParameterPredicate.class
        );
        return predicate.test(parameterContext, extensionContext);
    }

    /**
     * Resolves and provides the value for a test method parameter.
     * <p>
     * JUnit 5 calls this method to get the actual argument value for a test
     * method parameter. This method uses the {@link ResolutionContext} to
     * generate and supply a suitable value for the parameter.
     * </p>
     *
     * @param parameterContext the context for the parameter to be resolved
     * @param extensionContext the current extension context provided by JUnit
     * @return the generated argument value for the parameter
     * @throws ParameterResolutionException if the parameter cannot be resolved
     * @see AutoParams
     * @see ResolutionContext
     */
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
