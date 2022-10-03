package autoparams.generator;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

final class OffsetDateTimeGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(OffsetDateTime.class)
            ? new ObjectContainer(factory(context))
            : ObjectContainer.EMPTY;
    }

    private OffsetDateTime factory(ObjectGenerationContext context) {
        int bound = (int) TimeUnit.DAYS.toSeconds(7);
        int seconds = ThreadLocalRandom.current().nextInt(bound);
        Clock clock = context.generate(Clock.class);
        return OffsetDateTime.now(clock).minusSeconds(seconds);
    }

}
