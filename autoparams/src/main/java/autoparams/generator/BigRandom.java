package autoparams.generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class BigRandom {

    // Computes number of bits needed to represent an n digit positive integer.

    private static int bitCount(int n) {
        return (int) Math.ceil(n * Math.log(10) / Math.log(2));
    }

    static BigDecimal between(BigDecimal min, BigDecimal max) {
        return between(min, max, ThreadLocalRandom.current());
    }

    private static BigDecimal between(BigDecimal min, BigDecimal max, Random r) {
        return min.add(
            nextBigDecimal(max.subtract(min), Math.max(min.precision(), max.precision()), r));
    }

    private static BigDecimal nextBigDecimal(int scale) {
        return nextBigDecimal(scale, ThreadLocalRandom.current());
    }

    private static BigDecimal nextBigDecimal(int scale, Random r) {
        BigInteger bi = new BigInteger(bitCount(scale), r);
        BigDecimal bd = new BigDecimal(bi);
        return bd.movePointLeft(bd.precision());
    }

    private static BigDecimal nextBigDecimal(BigDecimal norm, int scale, Random r) {
        return norm.multiply(nextBigDecimal(scale, r));
    }

}
