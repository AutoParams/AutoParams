package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class DefaultURLGenerationOptionsProvider
    extends ObjectGeneratorBase<URIGenerationOptions> {

    @Override
    protected URIGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return URIGenerationOptions.DEFAULT;
    }
}
