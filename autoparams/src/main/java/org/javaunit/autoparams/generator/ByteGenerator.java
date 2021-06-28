package org.javaunit.autoparams.generator;

import static java.lang.Byte.MAX_VALUE;
import static java.lang.Byte.MIN_VALUE;

import java.lang.reflect.Type;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

final class ByteGenerator implements ObjectGenerator {
    static final String BAD_RANGE = "Max must be greater than Min";
    static final String BAD_MIN_BOUND = "Min must be >= Byte.MIN_VALUE and <= Byte.MAX_VALUE";
    static final String BAD_MAX_BOUND = "Max must be >= Byte.MIN_VALUE and <= Byte.MAX_VALUE";

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == byte.class || type == Byte.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private byte getMin(ObjectQuery query) {
        return query instanceof ArgumentQuery ? getMin((ArgumentQuery) query) : MIN_VALUE;
    }

    private byte getMin(ArgumentQuery query) {
        Min annotation = query.getParameter().getAnnotation(Min.class);
        if (annotation == null) {
            return MIN_VALUE;
        } else if (annotation.value() < MIN_VALUE || annotation.value() > MAX_VALUE) {
            throw new IllegalArgumentException(BAD_MIN_BOUND);
        } else {
            return (byte) annotation.value();
        }
    }

    private byte getMax(ObjectQuery query) {
        return query instanceof ArgumentQuery ? getMax((ArgumentQuery) query) : MAX_VALUE;
    }

    private byte getMax(ArgumentQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        if (annotation == null) {
            return MAX_VALUE;
        } else if (annotation.value() < MIN_VALUE || annotation.value() > MAX_VALUE) {
            throw new IllegalArgumentException(BAD_MAX_BOUND);
        } else {
            return (byte) annotation.value();
        }
    }

    private byte factory(byte min, byte max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min >= max) {
            throw new IllegalArgumentException(BAD_RANGE);
        }

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return (byte) random.nextInt(MIN_VALUE, MAX_VALUE);
        }

        int offset = max == MAX_VALUE ? -1 : 0;
        int origin = min + offset;
        int bound = max + 1 + offset;
        return (byte) (random.nextInt(origin, bound) - offset);
    }
}
