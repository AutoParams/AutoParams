package autoparams.generator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import autoparams.ResolutionContext;

final class ZonedDateTimeGenerator extends PlainObjectGenerator<ZonedDateTime> {

    ZonedDateTimeGenerator() {
        super(ZonedDateTime.class);
    }

    @Override
    protected ZonedDateTime generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        LocalDateTime localDateTime = context.resolve(LocalDateTime.class);
        ZoneId zoneId = context.resolve(ZoneId.class);
        return localDateTime.atZone(zoneId);
    }
}
