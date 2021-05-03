package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Min;

final class DoubleGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == double.class || type == Double.class) {
            double origin = getOrigin(query);
            double value = ThreadLocalRandom.current().nextDouble(origin, Double.MAX_VALUE);
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

}
