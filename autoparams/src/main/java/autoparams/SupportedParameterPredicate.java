package autoparams;

import java.util.function.BiPredicate;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

@FunctionalInterface
public interface SupportedParameterPredicate
    extends BiPredicate<ParameterContext, ExtensionContext> {

    SupportedParameterPredicate DEFAULT = new TestGearBlock();
}
