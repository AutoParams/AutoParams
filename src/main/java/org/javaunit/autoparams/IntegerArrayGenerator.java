package org.javaunit.autoparams;

import java.util.Optional;

final class IntegerArrayGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(int[].class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(new int[] {
            IntegerGenerator.factory(),
            IntegerGenerator.factory(),
            IntegerGenerator.factory()
        });
    }
    
}
