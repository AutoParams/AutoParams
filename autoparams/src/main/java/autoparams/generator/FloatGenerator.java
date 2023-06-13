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

    private float getBound(ObjectQuery query) {
        if (query instanceof ParameterQuery) {
            ParameterQuery argumentQuery = (ParameterQuery) query;
            Max max = argumentQuery.getParameter().getAnnotation(Max.class);
            if (max != null) {
                return max.value();
            }
        }

        return 1.0f;
    }

    private float getOrigin(ObjectQuery query) {
        if (query instanceof ParameterQuery) {
            ParameterQuery argumentQuery = (ParameterQuery) query;
            Parameter parameter = argumentQuery.getParameter();
            Min min = parameter.getAnnotation(Min.class);
            if (min != null) {
                Max max = parameter.getAnnotation(Max.class);
                if (max == null) {
                    throw new RuntimeException(
                        "The parameter annotated with @Min is missing the"
                        + " required @Max annotation. Please annotate the"
                        + " parameter with both @Min and @Max annotations to"
                        + " specify the minimum and maximum allowed values.");
                }
                return min.value();
            }
        }

        return 0.0f;
    }

}
