package autoparams.generator;

import java.time.Instant;
import java.time.OffsetDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class InstantGenerator extends ObjectGeneratorBase<Instant> {

    @Override
    protected Instant generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(OffsetDateTime.class).toInstant();
    }
}
