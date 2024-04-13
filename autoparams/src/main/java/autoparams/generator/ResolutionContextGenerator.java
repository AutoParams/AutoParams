package autoparams.generator;

import autoparams.ResolutionContext;

final class ResolutionContextGenerator
    extends PlainObjectGenerator<ResolutionContext> {

    ResolutionContextGenerator() {
        super(ResolutionContext.class);
    }

    @Override
    protected ResolutionContext generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context;
    }
}
