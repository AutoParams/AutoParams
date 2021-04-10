package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Min;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == int.class || type == Integer.class) {
            int origin = getOrigin(query);
            int value = ThreadLocalRandom.current().nextInt(origin, Integer.MAX_VALUE);
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

}
