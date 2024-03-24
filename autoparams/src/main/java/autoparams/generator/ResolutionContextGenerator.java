package autoparams.generator;

import autoparams.ResolutionContext;

final class ResolutionContextGenerator extends TypeMatchingGenerator {

    public ResolutionContextGenerator() {
        super((query, context) -> context, ResolutionContext.class);
    }
}
