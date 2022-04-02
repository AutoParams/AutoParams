package org.javaunit.autoparams.generator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

final class OffsetDateTimeGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(OffsetDateTime.class)
            ? new ObjectContainer(factory(context))
            : ObjectContainer.EMPTY;
    }

    private OffsetDateTime factory(ObjectGenerationContext context) {
        ZoneId zoneId = context.generate(ZoneId.class);
        Instant instant = context.generate(Instant.class);

        return OffsetDateTime.ofInstant(instant, zoneId);
    }

}
