package autoparams.generator;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

final class BigDecimalGenerator extends TypeMatchingGenerator {

    BigDecimalGenerator() {
        super((query, context) -> factory(query), BigDecimal.class, Number.class);
    }

    private static BigDecimal factory(ObjectQuery query) {
        return factory(getMin(query), getMax(query));
    }

    private static BigDecimal factory(long min, long max) {
        if (max < min) {
            throw new IllegalArgumentException("@Max must be greater than or equal to @Min");
        }
        if (min == max) {
            return BigDecimal.valueOf(min);
        }
        return BigRandom.between(BigDecimal.valueOf(min), BigDecimal.valueOf(max));
    }

    private static long getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
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
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private static long getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
        return max == null ? MAX_VALUE : max.value();
    }
}
