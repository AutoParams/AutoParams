package autoparams.generator;

import autoparams.ResolutionContext;

final class ConstructorExtractorGenerator
    extends ObjectGeneratorBase<ConstructorExtractor> {

    @Override
    protected ConstructorExtractor generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return new DefaultConstructorExtractor();
    }
}
