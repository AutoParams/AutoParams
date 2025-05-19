package autoparams;

import java.util.function.BiPredicate;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

/**
 * Predicate for determining whether a parameter is supported for parameter
 * resolution.
 * <p>
 * This functional interface extends {@link BiPredicate} and is used by
 * {@link AutoParamsExtension} to decide if a test method parameter can be
 * automatically generated and injected.
 * </p>
 *
 * @see AutoParamsExtension#supportsParameter(ParameterContext, ExtensionContext)
 */
@FunctionalInterface
public interface SupportedParameterPredicate
    extends BiPredicate<ParameterContext, ExtensionContext> {

    /**
     * Default predicate for parameter resolution filtering.
     * <p>
     * This constant provides a standard implementation that determines which
     * parameters are eligible for automatic value generation. It ensures that
     * framework-specific parameters are handled appropriately during the
     * parameter resolution process.
     * </p>
     */
    SupportedParameterPredicate DEFAULT = new TestGearBlock();
}
