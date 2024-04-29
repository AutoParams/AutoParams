package autoparams.generator;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class YearGenerator extends ObjectGeneratorBase<Year> {

    @Override
    protected Year generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Year thisYear = Year.now();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return Year.of(thisYear.getValue() + random.nextInt(-10, 11));
    }
}
