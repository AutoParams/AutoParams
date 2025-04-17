package autoparams.generator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class ZonedDateTimeGenerator extends ObjectGeneratorBase<ZonedDateTime> {

    @Override
    protected ZonedDateTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return ZonedDateTime
            .now(context.resolve(ZoneId.class))
            .minusDays(random.nextInt(0, 365))
            .minusNanos(random.nextLong(0, 24 * 60 * 60 * 1_000_000_000L));
    }
}
