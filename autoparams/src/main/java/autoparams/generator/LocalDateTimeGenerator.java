package autoparams.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class LocalDateTimeGenerator extends ObjectGeneratorBase<LocalDateTime> {

    @Override
    protected LocalDateTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return LocalDateTime.of(
            context.resolve(LocalDate.class),
            context.resolve(LocalTime.class)
        );
    }
}
