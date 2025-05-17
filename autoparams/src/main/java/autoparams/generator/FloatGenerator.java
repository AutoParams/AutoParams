package autoparams.generator;

import java.lang.reflect.Parameter;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

final class FloatGenerator extends PrimitiveTypeGenerator<Float> {

    FloatGenerator() {
        super(float.class, Float.class);
    }

    @Override
    protected Float generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        float origin = getOrigin(query);
        float bound = getBound(query);
        return (float) ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    private static float getOrigin(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getOrigin((ParameterQuery) query)
            : 0.0f;
    }

    private static float getOrigin(ParameterQuery query) {
        Parameter parameter = query.getParameter();
        Min min = parameter.getAnnotation(Min.class);
        if (min == null) {
            Max max = parameter.getAnnotation(Max.class);
            return max == null || max.value() > 0 ? 0.0f : Long.MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private static float getBound(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getBound((ParameterQuery) query)
            : 1.0f;
    }

    private static float getBound(ParameterQuery query) {
        Parameter parameter = query.getParameter();
        Max max = parameter.getAnnotation(Max.class);
        if (max == null) {
            Min min = parameter.getAnnotation(Min.class);
            return min == null || min.value() < 0 ? 1.0f : Long.MAX_VALUE;
        } else {
            return max.value();
        }
    }
}
