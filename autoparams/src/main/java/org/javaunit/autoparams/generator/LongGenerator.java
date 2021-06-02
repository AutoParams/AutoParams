package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class LongGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        if (type == long.class || type == Long.class) {
            long origin = getOrigin(query);
            long bound = getBound(query);
            if (origin == bound) {
                return new ObjectContainer(origin);
            }
            long value = ThreadLocalRandom.current().nextLong(origin, bound);
            return new ObjectContainer(value);
        }

        return ObjectContainer.EMPTY;
    }

    private long getBound(ObjectQuery query) {
        if (query instanceof ArgumentQuery) {
            ArgumentQuery argumentQuery = (ArgumentQuery) query;
            Max annotation = argumentQuery.getParameter().getAnnotation(Max.class);
            if (annotation != null) {
                return annotation.value();
            }
        }

        return Long.MAX_VALUE;
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
