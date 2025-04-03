package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.MaxAnnotation.findMaxAnnotation;
import static autoparams.generator.MinAnnotation.findMinAnnotation;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

final class LongGenerator extends PrimitiveTypeGenerator<Long> {

    LongGenerator() {
        super(long.class, Long.class);
    }

    @Override
    protected Long generateValue(ObjectQuery query, ResolutionContext context) {
        long min = getMin(query);
        long max = getMax(query);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return random.nextLong();
        }

        long offset = max == MAX_VALUE ? -1 : 0;
        long origin = min + offset;
        long bound = max + offset + 1;
        return random.nextLong(origin, bound) - offset;
    }

    private static long getMin(ObjectQuery query) {
        Min min = findMinAnnotation(query);
        if (min == null) {
            Max max = findMaxAnnotation(query);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private static long getMax(ObjectQuery query) {
        Max max = findMaxAnnotation(query);
        return max == null ? MAX_VALUE : max.value();
    }
}
