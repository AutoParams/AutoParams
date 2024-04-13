package autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;

final class StringGenerator extends PlainObjectGenerator<String> {

    StringGenerator() {
        super(String.class);
    }

    @Override
    protected String generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return UUID.randomUUID().toString();
    }
}
