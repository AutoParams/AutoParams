package autoparams.generator;

import java.time.LocalTime;
import java.time.ZonedDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class LocalTimeGenerator extends ObjectGeneratorBase<LocalTime> {

    @Override
    protected LocalTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(ZonedDateTime.class).toLocalTime();
    }
}
