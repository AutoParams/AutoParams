package org.javaunit.autoparams;

import java.util.Optional;

final class CharacterGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(char.class) || type.equals(Character.class)
            ? factory()
            : Optional.empty();
    }

    private Optional<Object> factory() {
        int exclusiveCharUpperBound = Character.MAX_VALUE + 1;
        return Optional.of((char) RANDOM.nextInt(exclusiveCharUpperBound));
    }

}
