package autoparams.generator;

import java.time.Clock;

import autoparams.ResolutionContext;

final class ClockGenerator extends ObjectGeneratorBase<Clock> {

    @Override
    protected Clock generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return Clock.systemDefaultZone();
    }
}
