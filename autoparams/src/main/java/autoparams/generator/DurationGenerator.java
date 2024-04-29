package autoparams.generator;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class DurationGenerator extends ObjectGeneratorBase<Duration> {

    @Override
    protected Duration generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int hours = random.nextInt(1, 48 + 1);
        return Duration.ofHours(hours);
    }
}
