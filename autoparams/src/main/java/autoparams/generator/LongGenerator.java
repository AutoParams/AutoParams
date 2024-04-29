package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

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
        long bound = max + 1 + offset;
        return random.nextLong(origin, bound) - offset;
    }

    private static long getMin(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMin((ParameterQuery) query)
            : MIN_VALUE;
    }

    private static long getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            Max max = query.getParameter().getAnnotation(Max.class);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private static long getMax(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMax((ParameterQuery) query)
            : MAX_VALUE;
    }

    private static long getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
        return max == null ? MAX_VALUE : max.value();
    }
}
