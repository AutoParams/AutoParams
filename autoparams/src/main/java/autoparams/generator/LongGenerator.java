package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class LongGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == long.class || type == Long.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private long getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : Long.MIN_VALUE;
    }

    private long getMin(ParameterQuery query) {
        Min annotation = query.getParameter().getAnnotation(Min.class);
        return annotation == null
            ? Long.MIN_VALUE
            : annotation.value();
    }

    private long getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : Long.MAX_VALUE;
    }

    private long getMax(ParameterQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        return annotation == null
            ? Long.MAX_VALUE
            : annotation.value();
    }

    private long factory(long min, long max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == Long.MIN_VALUE && max == Long.MAX_VALUE) {
            return random.nextLong();
        }

        long offset = max == Long.MAX_VALUE ? -1 : 0;
        long origin = min + offset;
        long bound = max + 1 + offset;
        return random.nextLong(origin, bound) - offset;
    }

}
