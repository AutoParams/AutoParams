package org.javaunit.autoparams;

import java.util.Optional;

final class IntegerGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(int.class) || type.equals(Integer.class) ? Optional.of(factory()) : Optional.empty();
    }

    public static int factory() {
        return RANDOM.nextInt();
    }

}
