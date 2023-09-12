package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Byte.MAX_VALUE;
import static java.lang.Byte.MIN_VALUE;

final class ByteGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == byte.class || type == Byte.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private byte getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
    }

    private byte getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            return MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (byte) min.value();
        }
    }

    private byte getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private byte getMax(ParameterQuery query) {
        Max max = query.getParameter().getAnnotation(Max.class);
        if (max == null) {
            return MAX_VALUE;
        } else if (max.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The max constraint underflowed.");
        } else if (max.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The max constraint overflowed.");
        } else {
            return (byte) max.value();
        }
    }

    private byte factory(byte min, byte max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return (byte) random.nextInt(MIN_VALUE, MAX_VALUE);
        }

        int offset = max == MAX_VALUE ? -1 : 0;
        int origin = min + offset;
        int bound = max + 1 + offset;
        return (byte) (random.nextInt(origin, bound) - offset);
    }
}
