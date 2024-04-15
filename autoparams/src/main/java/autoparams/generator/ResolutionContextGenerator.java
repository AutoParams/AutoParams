package autoparams.generator;

import autoparams.ResolutionContext;

final class ResolutionContextGenerator
    extends ObjectGeneratorBase<ResolutionContext> {

    @Override
    protected ResolutionContext generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context;
    }
}
