package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Short.MAX_VALUE;
import static java.lang.Short.MIN_VALUE;

final class ShortGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == short.class || type == Short.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private short getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
    }

    private short getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            Max max = query.getParameter().getAnnotation(Max.class);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (short) min.value();
        }
    }

    private short getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private short getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
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

    private short factory(short min, short max) {
        return (short) ThreadLocalRandom.current().nextInt(min, (max + 1));
    }
}
