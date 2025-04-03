package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.MaxAnnotation.findMaxAnnotation;
import static autoparams.generator.MinAnnotation.findMinAnnotation;
import static java.lang.Short.MAX_VALUE;
import static java.lang.Short.MIN_VALUE;

final class ShortGenerator extends PrimitiveTypeGenerator<Short> {

    ShortGenerator() {
        super(short.class, Short.class);
    }

    @Override
    protected Short generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        short min = getMin(query);
        short max = getMax(query);
        return (short) ThreadLocalRandom.current().nextInt(min, (max + 1));
    }

    private static short getMin(ObjectQuery query) {
        Min min = findMinAnnotation(query);
        if (min == null) {
            Max max = findMaxAnnotation(query);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (short) min.value();
        }
    }

    private static short getMax(ObjectQuery query) {
        Max max = findMaxAnnotation(query);
        if (max == null) {
            return MAX_VALUE;
        } else if (max.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (max.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (short) max.value();
        }
    }
}
