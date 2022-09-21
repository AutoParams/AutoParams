package org.javaunit.autoparams.generator;

import java.time.Clock;
import java.time.ZoneId;

final class ClockGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {

        return query.getType().equals(Clock.class)
            ? new ObjectContainer(Clock.system(context.generate(ZoneId.class)))
            : ObjectContainer.EMPTY;
    }

}
