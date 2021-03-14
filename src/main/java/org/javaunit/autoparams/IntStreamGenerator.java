/*
    Contributors:
        Aaron(JIN, Taeyang) - Create IntStreamGenerator
*/

package org.javaunit.autoparams;

import java.util.Optional;
import java.util.stream.IntStream;

public class IntStreamGenerator implements ObjectGenerator {
    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.equals(IntStream.class) ? factory() : Optional.empty();
    }

    private Optional<Object> factory() {
        return Optional.of(RANDOM.ints(3));
    }
}
