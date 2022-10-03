package autoparams.generator;

import java.time.Clock;

final class ClockGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(Clock.class)
            ? new ObjectContainer(Clock.systemDefaultZone())
            : ObjectContainer.EMPTY;
    }

}
