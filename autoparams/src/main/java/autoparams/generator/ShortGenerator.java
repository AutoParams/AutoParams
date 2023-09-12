package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Short.MAX_VALUE;
import static java.lang.Short.MIN_VALUE;

final class ShortGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == short.class || type == Short.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private short getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
    }

    private short getMin(ParameterQuery query) {
        Min annotation = query.getParameter().getAnnotation(Min.class);
        return annotation == null ? MIN_VALUE : convertToShort(annotation.value());
    }

    private short getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private short getMax(ParameterQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        return annotation == null ? MAX_VALUE : convertToShort(annotation.value());
    }

    private short convertToShort(long value) {
        assertThatValueIsGreaterThatOrEqualToMinValue(value);
        assertThatValueIsLessThanOrEqualToMaxValue(value);
        return (short) value;
    }

    private void assertThatValueIsGreaterThatOrEqualToMinValue(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("the value is less than the lower bound.");
        }
    }

    private void assertThatValueIsLessThanOrEqualToMaxValue(long value) {
        if (value > MAX_VALUE) {
            throw new IllegalArgumentException("The value is greater than the upper bound.");
        }
    }

    private short factory(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (short) random.nextInt(min, (max + 1));
    }

}
