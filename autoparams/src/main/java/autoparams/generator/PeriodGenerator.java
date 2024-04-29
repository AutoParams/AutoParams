package autoparams.generator;

import java.time.LocalDate;
import java.time.Period;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class PeriodGenerator extends ObjectGeneratorBase<Period> {

    @Override
    protected Period generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        final Period period = Period.between(
            context.resolve(LocalDate.class),
            context.resolve(LocalDate.class)
        );
        return period.isNegative() ? period.negated() : period;
    }
}
