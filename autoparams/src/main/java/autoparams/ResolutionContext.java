package autoparams;

import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ResolutionContext extends ObjectGenerationContext {

    public ResolutionContext(
        ExtensionContext extensionContext,
        ObjectGenerator generator
    ) {
        super(extensionContext, generator);
    }
}
