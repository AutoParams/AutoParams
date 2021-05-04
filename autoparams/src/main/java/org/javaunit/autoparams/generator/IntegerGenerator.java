package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == int.class || type == Integer.class) {
            int origin = getOrigin(query);
            int maximum = getMaximum(query);
            int value = ThreadLocalRandom.current().nextInt(origin, maximum);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private int getOrigin(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Min annotation = argumentQuery.getParameter().getAnnotation(Min.class);
            if (annotation != null) {
                return (int) Math.max(Integer.MIN_VALUE, annotation.value());
            }
        }

        return Integer.MIN_VALUE;
    }

    private int getMaximum(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Max annotation = argumentQuery.getParameter().getAnnotation(Max.class);
            if (annotation != null) {
                return (int) Math.min(Integer.MAX_VALUE, annotation.value());
            }
        }

        return Integer.MAX_VALUE;
    }

}
