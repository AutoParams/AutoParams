package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

import static java.lang.Byte.MAX_VALUE;
import static java.lang.Byte.MIN_VALUE;

final class ByteGenerator extends PrimitiveTypeGenerator<Byte> {

    ByteGenerator() {
        super(byte.class, Byte.class);
    }

    @Override
    protected Byte generateValue(ObjectQuery query, ResolutionContext context) {
        byte min = getMin(query);
        byte max = getMax(query);
        return (byte) ThreadLocalRandom.current().nextInt(min, (max + 1));
    }

    private static byte getMin(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMin((ParameterQuery) query)
            : MIN_VALUE;
    }

    private static byte getMin(ParameterQuery query) {
        Min min = query.getParameter().getAnnotation(Min.class);
        if (min == null) {
            Max max = query.getParameter().getAnnotation(Max.class);
            return max == null || max.value() >= 1 ? 1 : MIN_VALUE;
        } else if (min.value() < MIN_VALUE) {
            throw new IllegalArgumentException("The min constraint underflowed.");
        } else if (min.value() > MAX_VALUE) {
            throw new IllegalArgumentException("The min constraint overflowed.");
        } else {
            return (byte) min.value();
        }
    }

    private static byte getMax(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getMax((ParameterQuery) query)
            : MAX_VALUE;
    }

    private static byte getMax(ParameterQuery query) {
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
}
