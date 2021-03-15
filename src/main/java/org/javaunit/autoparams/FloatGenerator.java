package org.javaunit.autoparams;

import java.util.Optional;
import org.javaunit.autoparams.range.FloatRange;
import org.javaunit.autoparams.range.RangeSupport;

final class FloatGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Optional<FloatRange> range = query.findAnnotation(FloatRange.class);
        Class<?> type = query.getType();
        return type.equals(float.class) || type.equals(Float.class) ? Optional.of(factory(range)) : Optional.empty();
    }

    private float factory(Optional<FloatRange> range) {
        return range.map(r -> RangeSupport.floatSupport(r))
                .map(r -> r.valueInRange(RANDOM))
                .orElseGet(RANDOM::nextFloat);
    }

}
