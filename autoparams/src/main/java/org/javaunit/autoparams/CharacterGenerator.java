package org.javaunit.autoparams;

import java.util.Optional;

public class CharacterGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(char.class) || type.equals(Character.class)
            ? factory()
            : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of((char) RANDOM.nextInt(Character.MAX_VALUE + 1));
    }

}
