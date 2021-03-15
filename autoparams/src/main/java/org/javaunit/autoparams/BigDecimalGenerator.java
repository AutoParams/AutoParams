package org.javaunit.autoparams;

import java.math.BigDecimal;
import java.util.Optional;

final class BigDecimalGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(BigDecimal.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(new BigDecimal(RANDOM.nextInt()));
    }

}
