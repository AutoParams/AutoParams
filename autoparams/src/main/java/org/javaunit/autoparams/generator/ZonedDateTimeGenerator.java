package org.javaunit.autoparams.generator;

import java.time.LocalDateTime;
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
        LocalDateTime localDateTime = context.generate(LocalDateTime.class);
        ZoneId zoneId = context.generate(ZoneId.class);
        return localDateTime.atZone(zoneId);
    }
}
