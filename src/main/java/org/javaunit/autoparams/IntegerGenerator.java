package org.javaunit.autoparams;

import java.util.Optional;
import org.javaunit.autoparams.range.IntRange;
import org.javaunit.autoparams.range.RangeSupport;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Optional<IntRange> range = query.findAnnotation(IntRange.class);
        Class<?> type = query.getType();
        return type.equals(int.class) || type.equals(Integer.class) ? Optional.of(factory(range)) : Optional.empty();
    }

    private int factory(Optional<IntRange> range) {
        return range.map(r -> RangeSupport.integerSupport(r))
        .map(r -> r.valueInRange(RANDOM))
        .orElseGet(RANDOM::nextInt);
    }

}
