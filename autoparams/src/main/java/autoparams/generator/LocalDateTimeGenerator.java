package autoparams.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import autoparams.ResolutionContext;

final class LocalDateTimeGenerator extends PlainObjectGenerator<LocalDateTime> {

    LocalDateTimeGenerator() {
        super(LocalDateTime.class);
    }

    @Override
    protected LocalDateTime generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return LocalDateTime.of(
            context.resolve(LocalDate.class),
            context.resolve(LocalTime.class)
        );
    }
}
