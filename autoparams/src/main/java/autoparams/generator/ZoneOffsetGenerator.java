package autoparams.generator;

import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class ZoneOffsetGenerator extends ObjectGeneratorBase<ZoneOffset> {

    @Override
    protected ZoneOffset generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int totalSeconds = random.nextInt(-18 * 3600, 18 * 3600);
        return ZoneOffset.ofTotalSeconds(totalSeconds);
    }
}
