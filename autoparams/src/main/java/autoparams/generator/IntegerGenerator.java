package autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type == int.class || type == Integer.class
            ? new ObjectContainer(factory(getMin(query), getMax(query)))
            : ObjectContainer.EMPTY;
    }

    private int getMin(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMin((ParameterQuery) query) : MIN_VALUE;
    }

    private int getMin(ParameterQuery query) {
        Min annotation = query.getParameter().getAnnotation(Min.class);
        return annotation == null
            ? MIN_VALUE
            : (int) Math.min(Math.max(annotation.value(), MIN_VALUE), MAX_VALUE);
    }

    private int getMax(ObjectQuery query) {
        return query instanceof ParameterQuery ? getMax((ParameterQuery) query) : MAX_VALUE;
    }

    private int getMax(ParameterQuery query) {
        Max annotation = query.getParameter().getAnnotation(Max.class);
        return annotation == null
            ? MAX_VALUE
            : (int) Math.max(Math.min(annotation.value(), MAX_VALUE), MIN_VALUE);
    }

    private int factory(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (min == MIN_VALUE && max == MAX_VALUE) {
            return random.nextInt();
        }

        int offset = max == MAX_VALUE ? -1 : 0;
        int origin = min + offset;
        int bound = max + 1 + offset;
        return random.nextInt(origin, bound) - offset;
    }

}
