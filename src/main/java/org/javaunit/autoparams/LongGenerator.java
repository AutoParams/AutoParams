package org.javaunit.autoparams;

import java.util.Optional;
import org.javaunit.autoparams.range.LongRange;
import org.javaunit.autoparams.range.RangeSupport;

final class LongGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Optional<LongRange> range = query.findAnnotation(LongRange.class);
        Class<?> type = query.getType();
        return type.equals(long.class) || type.equals(Long.class) ? Optional.of(factory(range)) : Optional.empty();
    }

    private long factory(Optional<LongRange> range) {
        return range.map(r -> RangeSupport.longSupport(r))
                .map(r -> r.valueInRange(RANDOM))
                .orElseGet(RANDOM::nextLong);
    }

}
