package org.javaunit.autoparams;

import java.util.Optional;
import org.javaunit.autoparams.range.DoubleRange;
import org.javaunit.autoparams.range.RangeSupport;

final class DoubleGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Optional<DoubleRange> range = query.findAnnotation(DoubleRange.class);
        Class<?> type = query.getType();
        return type.equals(double.class) || type.equals(Double.class) ? Optional.of(factory(range)) : Optional.empty();
    }

    private double factory(Optional<DoubleRange> range) {
        return range.map(r -> RangeSupport.doubleSupport(r))
                .map(r -> r.valueInRange(RANDOM))
                .orElseGet(RANDOM::nextDouble);
    }

}
