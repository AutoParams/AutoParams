package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

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

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return random.nextInt();
        }

        int offset = max == MAX_VALUE ? -1 : 0;
        int origin = min + offset;
        int bound = max + 1 + offset;
        return random.nextInt(origin, bound) - offset;
    }

    private static int getMin(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMin((ParameterQuery) query)
            : MIN_VALUE;
    }

    private static int getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            Max max = query.getParameter().getAnnotation(Max.class);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (int) min.value();
        }
    }

    private static int getMax(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMax((ParameterQuery) query)
            : MAX_VALUE;
    }

    private static int getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
        if (max == null) {
            return MAX_VALUE;
        } else if (max.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (max.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (int) max.value();
        }
    }
}
