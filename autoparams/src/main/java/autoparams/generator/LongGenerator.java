package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import static autoparams.generator.MaxAnnotation.findMaxAnnotation;
import static autoparams.generator.MinAnnotation.findMinAnnotation;

final class LongGenerator extends PrimitiveTypeGenerator<Long> {

    LongGenerator() {
        super(long.class, Long.class);
    }

    @Override
    protected Long generateValue(ObjectQuery query, ResolutionContext context) {
        long min = getMin(query);
        long max = getMax(query);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == Long.MIN_VALUE && max == Long.MAX_VALUE) {
            return random.nextLong();
        }

        long offset = max == Long.MAX_VALUE ? -1 : 0;
        long origin = min + offset;
        long bound = max + offset + 1;
        return random.nextLong(origin, bound) - offset;
    }

    private static long getMin(ObjectQuery query) {
        Min min = findMinAnnotation(query);
        if (min == null) {
            Max max = findMaxAnnotation(query);
            return max == null || max.value() >= 1 ? 1 : Long.MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private static long getMax(ObjectQuery query) {
        Max max = findMaxAnnotation(query);
        return max == null ? Short.MAX_VALUE + 1 : max.value();
    }
}
