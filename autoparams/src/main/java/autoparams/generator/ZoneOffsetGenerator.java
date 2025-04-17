package autoparams.generator;

import java.time.ZoneOffset;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class ZoneOffsetGenerator extends ObjectGeneratorBase<ZoneOffset> {

    @Override
    protected ZoneOffset generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return ZoneOffset.UTC;
    }
}
