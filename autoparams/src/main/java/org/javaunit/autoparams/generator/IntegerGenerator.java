package org.javaunit.autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Min;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        if (query instanceof ArgumentQuery) {
            return generate((ArgumentQuery) query, context);
        }

        return ObjectContainer.EMPTY;
    }

    public ObjectContainer generate(ArgumentQuery query, ObjectGenerationContext context) {
        if (query.getType() == int.class) {
            Min annotation = query.getParameter().getAnnotation(Min.class);
            if (annotation != null) {
                int min = (int) Math.max(Integer.MIN_VALUE, annotation.value());
                int value = ThreadLocalRandom.current().nextInt(min, Integer.MAX_VALUE);
                return new ObjectContainer(value);
            }
        }

        return ObjectContainer.EMPTY;
    }
}
