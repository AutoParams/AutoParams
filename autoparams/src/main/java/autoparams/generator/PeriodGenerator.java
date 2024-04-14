package autoparams.generator;

import java.time.LocalDate;
import java.time.Period;

import autoparams.ResolutionContext;

final class PeriodGenerator extends PlainObjectGenerator<Period> {

    PeriodGenerator() {
        super(Period.class);
    }

    @Override
    protected Period generateValue(
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
