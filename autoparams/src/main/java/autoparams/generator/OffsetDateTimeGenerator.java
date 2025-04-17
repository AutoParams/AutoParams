package autoparams.generator;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class OffsetDateTimeGenerator
    extends ObjectGeneratorBase<OffsetDateTime> {

    @Override
    protected OffsetDateTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(ZonedDateTime.class).toOffsetDateTime();
    }
}
