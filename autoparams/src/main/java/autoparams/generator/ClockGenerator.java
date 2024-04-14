package autoparams.generator;

import java.time.Clock;

import autoparams.ResolutionContext;

final class ClockGenerator extends PlainObjectGenerator<Clock> {

    ClockGenerator() {
        super(Clock.class);
    }

    @Override
    protected Clock generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return Clock.systemDefaultZone();
    }
}
