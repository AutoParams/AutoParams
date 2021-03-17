package org.javaunit.autoparams;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class JavaTimeGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {

        if (query.getType().equals(LocalDate.class)) {
            return Optional.of(LocalDate.now());
        }

        if (query.getType().equals(LocalTime.class)) {
            return Optional.of(LocalTime.now());
        }

        if (query.getType().equals(LocalDateTime.class)) {
            return Optional.of(LocalDateTime.now());
        }

        return Optional.empty();
    }
}
