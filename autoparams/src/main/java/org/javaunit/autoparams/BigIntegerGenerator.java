package org.javaunit.autoparams;

import java.math.BigInteger;
import java.util.Optional;

final class BigIntegerGenerator implements ObjectGenerator {
    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(BigInteger.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        int maxBitLength = 256;
        return Optional.of(new BigInteger(maxBitLength, RANDOM));
    }
}
