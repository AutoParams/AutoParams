package org.javaunit.autoparams;

import java.util.Optional;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(int.class) || type.equals(Integer.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextInt());
    }

}
