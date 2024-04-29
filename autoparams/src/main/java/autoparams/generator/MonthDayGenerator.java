package autoparams.generator;

import java.time.LocalDate;
import java.time.MonthDay;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class MonthDayGenerator extends ObjectGeneratorBase<MonthDay> {

    @Override
    protected MonthDay generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return MonthDay.from(context.resolve(LocalDate.class));
    }
}
