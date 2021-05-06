package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Min;

final class LongGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == long.class || type == Long.class) {
            long origin = getOrigin(query);
            long value = ThreadLocalRandom.current().nextLong(origin, Long.MAX_VALUE);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private long getOrigin(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Min annotation = argumentQuery.getParameter().getAnnotation(Min.class);
            if (annotation != null) {
                return annotation.value();
            }
        }

        return Long.MIN_VALUE;
    }

}
