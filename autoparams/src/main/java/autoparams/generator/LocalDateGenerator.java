package autoparams.generator;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class LocalDateGenerator extends PlainObjectGenerator<LocalDate> {

    LocalDateGenerator() {
        super(LocalDate.class);
    }

    @Override
    protected LocalDate generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        final LocalDate today = LocalDate.now();
        return today.plusDays(ThreadLocalRandom.current().nextInt(-28, 29));
    }
}
