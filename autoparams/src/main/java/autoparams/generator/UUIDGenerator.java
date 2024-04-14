package autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;

final class UUIDGenerator extends PlainObjectGenerator<UUID> {

    UUIDGenerator() {
        super(UUID.class);
    }

    @Override
    protected UUID generateValue(ObjectQuery query, ResolutionContext context) {
        return UUID.randomUUID();
    }
}
