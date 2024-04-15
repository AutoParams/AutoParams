package autoparams.generator;

import autoparams.ResolutionContext;
import org.junit.jupiter.api.extension.ExtensionContext;

final class ExtensionContextGenerator
    extends ObjectGeneratorBase<ExtensionContext> {

    @Override
    protected ExtensionContext generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.getExtensionContext();
    }
}
