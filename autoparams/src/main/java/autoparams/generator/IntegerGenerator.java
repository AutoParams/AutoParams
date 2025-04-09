package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class IntegerGenerator extends PrimitiveTypeGenerator<Integer> {

    IntegerGenerator() {
        super(int.class, Integer.class);
    }

    @Override
    protected Integer generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        int min = getMin(query);
        int max = getMax(query);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == Integer.MIN_VALUE && max == Integer.MAX_VALUE) {
            return random.nextInt();
        }

        int offset = max == Integer.MAX_VALUE ? -1 : 0;
        int origin = min + offset;
        int bound = max + offset + 1;
        return random.nextInt(origin, bound) - offset;
    }

    private static int getMin(ObjectQuery query) {
        Min min = MinAnnotation.findMinAnnotation(query);
        if (min == null) {
            Max max = MaxAnnotation.findMaxAnnotation(query);
            return max == null || max.value() >= 1 ? 1 : Integer.MIN_VALUE;
        } else if (min.value() < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (int) min.value();
        }
    }

    private static int getMax(ObjectQuery query) {
        Max max = MaxAnnotation.findMaxAnnotation(query);
        if (max == null) {
            return Short.MAX_VALUE + 1;
        } else if (max.value() < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (max.value() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (int) max.value();
        }
    }
}
