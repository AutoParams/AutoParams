package autoparams.generator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class BigDecimalGenerator extends ObjectGeneratorBase<BigDecimal> {

    @Override
    protected BigDecimal generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return new BigDecimal(ThreadLocalRandom.current().nextInt());
    }
}
