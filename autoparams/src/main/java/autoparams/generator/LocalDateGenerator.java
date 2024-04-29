package autoparams.generator;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class LocalDateGenerator extends ObjectGeneratorBase<LocalDate> {

    @Override
    protected LocalDate generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        final LocalDate today = LocalDate.now();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return today.plusDays(random.nextInt(-365, 366));
    }
}
