package autoparams.generator;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class LocalDateGenerator extends ObjectGeneratorBase<LocalDate> {

    @Override
    protected LocalDate generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return context.resolve(ZonedDateTime.class).toLocalDate();
    }
}
