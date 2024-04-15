package autoparams.generator;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class LocalTimeGenerator extends ObjectGeneratorBase<LocalTime> {

    @Override
    protected LocalTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long inclusiveMin = LocalTime.MIN.toNanoOfDay();
        long exclusiveMax = LocalTime.MAX.toNanoOfDay() + 1;
        long nanoOfDay = random.nextLong(inclusiveMin, exclusiveMax);
        return LocalTime.ofNanoOfDay(nanoOfDay);
    }
}
