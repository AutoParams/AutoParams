package autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class DoubleGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == double.class || type == Double.class) {
            double origin = getOrigin(query);
            double bound = getBound(query);
            double value = ThreadLocalRandom.current().nextDouble(origin, bound);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private double getOrigin(ObjectQuery query) {
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

        return 0.0;
    }

    private double getBound(ObjectQuery query) {
        if (query instanceof ParameterQuery) {
            ParameterQuery argumentQuery = (ParameterQuery) query;
            Max annotation = argumentQuery.getParameter().getAnnotation(Max.class);
            if (annotation != null) {
                return annotation.value();
            }
        }

        return 1.0;
    }
}
