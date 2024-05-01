package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class DefaultURLGenerationOptionsProvider
    extends ObjectGeneratorBase<URLGenerationOptions> {

    @Override
    protected URLGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return URLGenerationOptions.DEFAULT;
    }
}
