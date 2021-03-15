package org.javaunit.autoparams.range;

import java.util.concurrent.ThreadLocalRandom;

public class RangeSupport<T extends Number> {
    private final RandomGetter<? extends T> randomGetter;

    public static RangeSupport<Integer> integerSupport(IntRange range) {
        return RangeSupport.integerSupport(range.min(), range.max());
    }

    public static RangeSupport<Integer> integerSupport(int min, int max) {
        return new RangeSupport<>(r -> r.nextInt(min, max+1));
    }

    public static RangeSupport<Double> doubleSupport(DoubleRange range) {
        return RangeSupport.doubleSupport(range.min(), range.max());
    }

    public static RangeSupport<Double> doubleSupport(double min, double max) {
        return new RangeSupport<>(r -> r.nextDouble(min, max+0.0000000000001D));
    }

    public static RangeSupport<Long> longSupport(LongRange range) {
        return RangeSupport.longSupport(range.min(), range.max());
    }

    public static RangeSupport<Long> longSupport(long min, long max) {
        return new RangeSupport<>(r -> r.nextLong(min, max+1));
    }

    public static RangeSupport<Float> floatSupport(FloatRange range) {
        return RangeSupport.floatSupport(range.min(), range.max());
    }

    public static RangeSupport<Float> floatSupport(float min, float max) {
        return new RangeSupport<>(r -> (float) r.nextDouble(min, max+0.0000000000001D));
    }

    private RangeSupport(RandomGetter<T> randomGetter) {
        this.randomGetter = randomGetter;
    }

    public T valueInRange(ThreadLocalRandom random) {
        return randomGetter.random(random);
    }

    private interface RandomGetter<T> {
        T random(ThreadLocalRandom random);
    }
}
