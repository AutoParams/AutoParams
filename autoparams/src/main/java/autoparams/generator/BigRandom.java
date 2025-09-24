package autoparams.generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class BigRandom {

    // Constants:
    private static final double log2 = Math.log10(2.0);

    // Computes number of bits needed to represent an n digit positive integer.

    private static int bitCount(int n) {
        return (int) (n / log2);
    }

    public static BigDecimal between(BigDecimal min, BigDecimal max, Random r) {
        return min.add(
            nextBigDecimal(max.subtract(min), Math.max(min.precision(), max.precision()), r));
    }

    public static BigDecimal between(BigDecimal min, BigDecimal max) {
        return between(min, max, ThreadLocalRandom.current());
    }

    // Static Methods for generating Random BigDecimal values:

    public static BigDecimal nextBigDecimal(int scale) {
        return nextBigDecimal(scale, ThreadLocalRandom.current());
    }

    public static BigDecimal nextBigDecimal(int scale, Random r) {
        BigInteger bi = new BigInteger(bitCount(scale), r);
        BigDecimal bd = new BigDecimal(bi);  // convert BigInteger to a BigDecimal
        return bd.movePointLeft(bd.precision());  // move the decimal point all the way to the left
    }

    public static BigDecimal nextBigDecimal(BigDecimal norm, int scale, Random r) {
        return norm.multiply(nextBigDecimal(scale, r));
    }

}
