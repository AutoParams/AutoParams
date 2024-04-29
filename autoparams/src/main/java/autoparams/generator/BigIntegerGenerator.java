package autoparams.generator;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class BigIntegerGenerator extends ObjectGeneratorBase<BigInteger> {

    @Override
    protected BigInteger generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        int maxBitLength = 256;
        return new BigInteger(maxBitLength, ThreadLocalRandom.current());
    }
}
