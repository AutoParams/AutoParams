package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

final class LongGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == long.class || type == Long.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private long getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
    }

    private long getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            Max max = query.getParameter().getAnnotation(Max.class);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private long getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private long getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
        return max == null ? MAX_VALUE : max.value();
    }

    private long factory(long min, long max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return random.nextLong();
        }

        long offset = max == MAX_VALUE ? -1 : 0;
        long origin = min + offset;
        long bound = max + 1 + offset;
        return random.nextLong(origin, bound) - offset;
    }
}
