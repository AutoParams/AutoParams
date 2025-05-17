package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import static autoparams.generator.MaxAnnotation.findMaxAnnotation;
import static autoparams.generator.MinAnnotation.findMinAnnotation;
import static java.lang.Byte.MAX_VALUE;
import static java.lang.Byte.MIN_VALUE;

final class ByteGenerator extends PrimitiveTypeGenerator<Byte> {

    ByteGenerator() {
        super(byte.class, Byte.class);
    }

    @Override
    protected Byte generateValue(ObjectQuery query, ResolutionContext context) {
        byte min = getMin(query);
        byte max = getMax(query);
        return (byte) ThreadLocalRandom.current().nextInt(min, (max + 1));
    }

    private static byte getMin(ObjectQuery query) {
        Min min = findMinAnnotation(query);
        if (min == null) {
            Max max = findMaxAnnotation(query);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (byte) min.value();
        }
    }

    private static byte getMax(ObjectQuery query) {
        Max max = findMaxAnnotation(query);
        if (max == null) {
            return MAX_VALUE;
        } else if (max.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (max.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (byte) max.value();
        }
    }
}
