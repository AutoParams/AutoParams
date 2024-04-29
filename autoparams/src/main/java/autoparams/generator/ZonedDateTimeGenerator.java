package autoparams.generator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class ZonedDateTimeGenerator extends ObjectGeneratorBase<ZonedDateTime> {

    @Override
    protected ZonedDateTime generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        LocalDateTime localDateTime = context.resolve(LocalDateTime.class);
        ZoneId zoneId = context.resolve(ZoneId.class);
        return localDateTime.atZone(zoneId);
    }
}
