package org.javaunit.autoparams;

import java.util.Optional;

final class BooleanGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(boolean.class) || type.equals(Boolean.class)
            ? GenerationResult.presence(factory())
            : GenerationResult.absence();
    }

    private Object factory() {
        return RANDOM.nextInt() % 2 == 0;
    }

}
