package autoparams;

import autoparams.generator.ObjectGeneratorBase;
import org.junit.jupiter.api.extension.ExtensionContext;

final class ExtensionContextProvider
    extends ObjectGeneratorBase<ExtensionContext> {

    private final ExtensionContext extensionContext;

    ExtensionContextProvider(ExtensionContext extensionContext) {
        this.extensionContext = extensionContext;
    }

    @Override
    protected ExtensionContext generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return extensionContext;
    }
}
