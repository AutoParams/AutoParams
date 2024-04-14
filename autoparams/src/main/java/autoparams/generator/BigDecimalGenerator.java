package autoparams.generator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class BigDecimalGenerator extends PlainObjectGenerator<BigDecimal> {

    BigDecimalGenerator() {
        super(BigDecimal.class);
    }

    @Override
    protected BigDecimal generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return new BigDecimal(ThreadLocalRandom.current().nextInt());
    }
}
