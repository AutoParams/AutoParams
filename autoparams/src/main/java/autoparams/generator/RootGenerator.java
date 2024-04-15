package autoparams.generator;

import autoparams.ResolutionContext;

final class RootGenerator extends ObjectGeneratorBase<Object> {

    @Override
    protected Object generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(String.class);
    }
}
