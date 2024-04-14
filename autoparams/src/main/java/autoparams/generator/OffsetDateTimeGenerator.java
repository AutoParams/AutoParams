package autoparams.generator;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import autoparams.ResolutionContext;

final class OffsetDateTimeGenerator
    extends PlainObjectGenerator<OffsetDateTime> {

    OffsetDateTimeGenerator() {
        super(OffsetDateTime.class);
    }

    @Override
    protected OffsetDateTime generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        int bound = (int) TimeUnit.DAYS.toSeconds(7);
        int seconds = ThreadLocalRandom.current().nextInt(bound);
        Clock clock = context.resolve(Clock.class);
        return OffsetDateTime.now(clock).plusSeconds(seconds);
    }
}
