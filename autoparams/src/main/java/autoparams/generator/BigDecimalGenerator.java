package autoparams.generator;

import java.math.BigDecimal;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import static autoparams.generator.MaxAnnotation.findMaxAnnotation;
import static autoparams.generator.MinAnnotation.findMinAnnotation;

final class BigDecimalGenerator extends ObjectGeneratorBase<BigDecimal> {

    @Override
    protected BigDecimal generateObject(ObjectQuery query, ResolutionContext context) {
        long min = getMin(query);
        long max = getMax(query);

        if (max < min) {
            throw new IllegalArgumentException("@Max must be greater than or equal to @Min");
        }
        if (min == max) {
            return BigDecimal.valueOf(min);
        }
        return BigRandom.between(BigDecimal.valueOf(min), BigDecimal.valueOf(max));
    }

    private static int getBound() {
        return 1000000 + 1;
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
        return max == null ? getBound() : max.value();
    }
}
