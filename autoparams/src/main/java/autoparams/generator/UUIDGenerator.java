package autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;

final class UUIDGenerator extends ObjectGeneratorBase<UUID> {

    @Override
    protected UUID generateObject(ObjectQuery query, ResolutionContext context) {
        return UUID.randomUUID();
    }
}
