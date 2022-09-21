package org.javaunit.autoparams.generator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeGenerator implements ObjectGenerator {
    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(ZonedDateTime.class)
            ? new ObjectContainer(factory(context))
            : ObjectContainer.EMPTY;
    }

    private ZonedDateTime factory(ObjectGenerationContext context) {
        Instant instant = context.generate(Instant.class);
        ZoneId zoneId = context.generate(ZoneId.class);
        return instant.atZone(zoneId);
    }
}
