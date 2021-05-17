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
            int bound = getBound(query);
            if (origin == bound) {
                return new ObjectContainer(origin);
            }
            int value = ThreadLocalRandom.current().nextInt(origin, bound);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private int getOrigin(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Min annotation = argumentQuery.getParameter().getAnnotation(Min.class);
            if (annotation != null) {
                long value = Math.min(Integer.MAX_VALUE, annotation.value());
                return (int) Math.max(Integer.MIN_VALUE, value);
            }
        }

        return Integer.MIN_VALUE;
    }

    private int getBound(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Max annotation = argumentQuery.getParameter().getAnnotation(Max.class);
            if (annotation != null) {
                long value = Math.max(Integer.MIN_VALUE, annotation.value());
                if (value < Long.MAX_VALUE) {
                    value = value + 1;
                }
                return (int) Math.min(Integer.MAX_VALUE, value);
            }
        }

        return Integer.MAX_VALUE;
    }

}
