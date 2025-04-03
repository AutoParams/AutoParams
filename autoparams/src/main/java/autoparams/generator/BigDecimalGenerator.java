package autoparams.generator;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class BigDecimalGenerator extends ObjectGeneratorBase<BigDecimal> {

    @Override
    protected BigDecimal generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Random random = ThreadLocalRandom.current();
        int value = random.nextInt(getOrigin(), getBound());
        return new BigDecimal(value);
    }

    private static int getOrigin() {
        return 1;
    }

    private static int getBound() {
        return 1000000 + 1;
    }
}
