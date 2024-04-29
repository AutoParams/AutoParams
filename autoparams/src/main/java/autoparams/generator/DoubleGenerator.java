package autoparams.generator;

import java.lang.reflect.Parameter;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class DoubleGenerator extends PrimitiveTypeGenerator<Double> {

    DoubleGenerator() {
        super(double.class, Double.class);
    }

    @Override
    protected Double generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        double origin = getOrigin(query);
        double bound = getBound(query);
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    private static double getOrigin(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getOrigin((ParameterQuery) query)
            : 0.0;
    }

    private static double getOrigin(ParameterQuery query) {
        Parameter parameter = query.getParameter();
        Min min = parameter.getAnnotation(Min.class);
        if (min == null) {
            Max max = parameter.getAnnotation(Max.class);
            return max == null || max.value() > 0 ? 0.0 : Long.MIN_VALUE;
        } else {
            return min.value();
        }
    }

    private static double getBound(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getBound((ParameterQuery) query)
            : 1.0;
    }

    private static double getBound(ParameterQuery query) {
        Parameter parameter = query.getParameter();
        Max max = parameter.getAnnotation(Max.class);
        if (max == null) {
            Min min = parameter.getAnnotation(Min.class);
            return min == null || min.value() < 0 ? 1.0 : Long.MAX_VALUE;
        } else {
            return max.value();
        }
    }
}
