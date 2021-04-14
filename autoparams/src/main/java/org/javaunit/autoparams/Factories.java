package org.javaunit.autoparams;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
final class Factories {

    public static IntStream createIntStream() {
        return IntStream.generate(Factories::createInt).limit(3);
    }

    public static LongStream createLongStream() {
        return LongStream.generate(Factories::createLong).limit(3);
    }

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    private static int createInt() {
        return random().nextInt();
    }

    private static long createLong() {
        return random().nextLong();
    }

    private static double createDouble() {
        return random().nextDouble();
    }

    public static DoubleStream createDoubleStream() {
        return DoubleStream.generate(Factories::createDouble).limit(3);
    }

}

