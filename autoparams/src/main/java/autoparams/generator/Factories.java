package autoparams.generator;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import autoparams.ResolutionContext;

final class Factories {

    public static IntStream createIntStream(ResolutionContext context) {
        return IntStream.generate(() -> context.resolve(int.class)).limit(3);
    }

    public static LongStream createLongStream(ResolutionContext context) {
        return LongStream.generate(() -> context.resolve(long.class)).limit(3);
    }

    public static DoubleStream createDoubleStream(ResolutionContext context) {
        return DoubleStream.generate(() -> context.resolve(double.class)).limit(3);
    }
}
