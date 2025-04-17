package autoparams.generator;

import java.time.ZoneId;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class ZoneIdGenerator extends ObjectGeneratorBase<ZoneId> {

    @Override
    protected ZoneId generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return ZoneId.systemDefault();
    }
}
