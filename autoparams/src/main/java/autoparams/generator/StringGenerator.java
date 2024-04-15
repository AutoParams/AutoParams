package autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;

final class StringGenerator extends ObjectGeneratorBase<String> {

    @Override
    protected String generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return UUID.randomUUID().toString();
    }
}
