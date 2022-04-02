package org.javaunit.autoparams.generator;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class InstantGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(Instant.class)
            ? new ObjectContainer(factory())
            : ObjectContainer.EMPTY;
    }

    private Instant factory() {
        int bound = (int) TimeUnit.DAYS.toSeconds(7);
        int seconds = ThreadLocalRandom.current().nextInt(bound);
        return Instant.now().minusSeconds(seconds);
    }
}
