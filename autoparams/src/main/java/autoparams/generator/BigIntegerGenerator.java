package autoparams.generator;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class BigIntegerGenerator extends PrimitiveTypeGenerator<BigInteger> {

    BigIntegerGenerator() {
        super(BigInteger.class, BigInteger.class);
    }

    @Override
    protected BigInteger generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        int maxBitLength = 256;
        return new BigInteger(maxBitLength, ThreadLocalRandom.current());
    }
}
