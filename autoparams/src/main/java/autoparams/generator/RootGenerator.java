package autoparams.generator;

import autoparams.ResolutionContext;

final class RootGenerator extends PlainObjectGenerator<Object> {

    RootGenerator() {
        super(Object.class);
    }

    @Override
    protected Object generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(String.class);
    }
}
