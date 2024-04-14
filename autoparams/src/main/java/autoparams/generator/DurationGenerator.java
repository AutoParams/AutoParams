package autoparams.generator;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class DurationGenerator extends PlainObjectGenerator<Duration> {

    DurationGenerator() {
        super(Duration.class);
    }

    @Override
    protected Duration generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int hours = random.nextInt(1, 48 + 1);
        return Duration.ofHours(hours);
    }
}
