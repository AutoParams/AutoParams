package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class URIGenerationOptionsProvider
    extends ObjectGeneratorBase<URIGenerationOptions> {

    private final URIGenerationOptions options;

    public URIGenerationOptionsProvider(URIGenerationOptions options) {
        this.options = options;
    }

    public URIGenerationOptionsProvider() {
        this(URIGenerationOptions.DEFAULT);
    }

    @Override
    protected URIGenerationOptions generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return options;
    }
}
