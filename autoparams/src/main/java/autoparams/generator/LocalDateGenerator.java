package autoparams.generator;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class LocalDateGenerator extends ObjectGeneratorBase<LocalDate> {

    @Override
    protected LocalDate generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        final LocalDate today = LocalDate.now();
        return today.plusDays(ThreadLocalRandom.current().nextInt(-28, 29));
    }
}
