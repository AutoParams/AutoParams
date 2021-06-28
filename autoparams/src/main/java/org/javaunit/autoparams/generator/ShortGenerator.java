package org.javaunit.autoparams.generator;

import static java.lang.Short.MAX_VALUE;
import static java.lang.Short.MIN_VALUE;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class ShortGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == short.class || type == Short.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private short getMin(ObjectQuery query) {
        return query instanceof ArgumentQuery ? getMin((ArgumentQuery) query) : MIN_VALUE;
    }

    private short getMin(ArgumentQuery query) {
        Min annotation = query.getParameter().getAnnotation(Min.class);
        return annotation == null
            ? MIN_VALUE
            : (short) Math.min(Math.max(annotation.value(), MIN_VALUE), MAX_VALUE);
    }

    private short getMax(ObjectQuery query) {
        return query instanceof ArgumentQuery ? getMax((ArgumentQuery) query) : MAX_VALUE;
    }

    private short getMax(ArgumentQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        return annotation == null
            ? MAX_VALUE
            : (short) Math.max(Math.min(annotation.value(), MAX_VALUE), MIN_VALUE);
    }

    private short factory(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (short) random.nextInt(min, (max + 1));
    }

}
