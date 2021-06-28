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
        if (annotation == null) {
            return MIN_VALUE;
        }
        return convertToShort(annotation.value());
    }

    private short getMax(ObjectQuery query) {
        return query instanceof ArgumentQuery ? getMax((ArgumentQuery) query) : MAX_VALUE;
    }

    private short getMax(ArgumentQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        if (annotation == null) {
            return MAX_VALUE;
        }
        return convertToShort(annotation.value());
    }

    private short convertToShort(long value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("value is out of range for short");
        }
        return (short) value;
    }

    private short factory(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (short) random.nextInt(min, (max + 1));
    }

}
