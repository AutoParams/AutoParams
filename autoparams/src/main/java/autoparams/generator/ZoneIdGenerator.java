package autoparams.generator;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class ZoneIdGenerator extends PlainObjectGenerator<ZoneId> {

    ZoneIdGenerator() {
        super(ZoneId.class);
    }

    @Override
    protected ZoneId generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        int index = ThreadLocalRandom.current().nextInt(0, zoneIds.size());
        return ZoneId.of(zoneIds.get(index));
    }
}
