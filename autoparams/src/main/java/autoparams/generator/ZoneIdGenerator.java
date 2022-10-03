package autoparams.generator;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ZoneIdGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(ZoneId.class)
            ? new ObjectContainer(factory())
            : ObjectContainer.EMPTY;
    }

    private ZoneId factory() {
        int randomZoneIdIndex = ThreadLocalRandom.current()
            .nextInt(0, ZoneId.getAvailableZoneIds().size());

        return Optional.of(ZoneId.getAvailableZoneIds())
            .map(ArrayList::new)
            .map(zoneIds -> zoneIds.get(randomZoneIdIndex))
            .map(ZoneId::of)
            .orElse(ZoneId.systemDefault());
    }
}
