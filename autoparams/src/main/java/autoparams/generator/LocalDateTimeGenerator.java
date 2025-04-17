package autoparams.generator;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class LocalDateTimeGenerator extends ObjectGeneratorBase<LocalDateTime> {

    @Override
    protected LocalDateTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(ZonedDateTime.class).toLocalDateTime();
    }
}
