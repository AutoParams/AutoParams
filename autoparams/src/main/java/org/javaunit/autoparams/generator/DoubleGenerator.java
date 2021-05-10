package org.javaunit.autoparams.generator;

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
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Min annotation = argumentQuery.getParameter().getAnnotation(Min.class);
            if (annotation != null) {
                return (double) Math.max(Double.MIN_VALUE, annotation.value());
            }
        }

        return Double.MIN_VALUE;
    }

    private double getBound(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Max annotation = argumentQuery.getParameter().getAnnotation(Max.class);
            if (annotation != null) {
                return (double) annotation.value();
            }
        }

        return Double.MAX_VALUE;
    }
}
