package autoparams.generator;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class LocalTimeGenerator extends PlainObjectGenerator<LocalTime> {

    LocalTimeGenerator() {
        super(LocalTime.class);
    }

    @Override
    protected LocalTime generateValue(
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
