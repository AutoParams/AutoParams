package org.javaunit.autoparams;

import java.util.Optional;

final class IntegerArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(ArgumentGenerationContext context) {
        Class<?> type = context.getParameter().getType();
        return type.equals(int.class) || type.equals(Integer.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.nextInt());
    }

}
