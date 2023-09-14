package autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class FloatGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == float.class || type == Float.class) {
            float origin = getOrigin(query);
            float bound = getBound(query);
            float value = (float) ThreadLocalRandom.current().nextDouble(origin, bound);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private float getOrigin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getOrigin((ParameterQuery) query) : 0.0f;
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

    private float getBound(ObjectQuery query) {
        return query instanceof ParameterQuery ? getBound((ParameterQuery) query) : 1.0f;
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
