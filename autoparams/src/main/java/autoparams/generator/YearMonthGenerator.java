package autoparams.generator;

import java.time.Year;
import java.time.YearMonth;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class YearMonthGenerator extends ObjectGeneratorBase<YearMonth> {

    @Override
    protected YearMonth generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Year year = context.resolve(Year.class);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return YearMonth.of(year.getValue(), 1 + random.nextInt(12));
    }
}
