package org.javaunit.autoparams;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

final class DateAndTimeGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {

        if (query.getType().equals(LocalDateTime.class)) {
            return Optional.of(factoryDateTime());
        }

        return Optional.empty();
    }

    private LocalDateTime factoryDateTime() {
        return LocalDateTime.of(factoryDate(), factoryTime());
    }

    private LocalTime factoryTime() {
        long inclusiveMin = LocalTime.MIN.toNanoOfDay();
        long exclusiveMax = LocalTime.MAX.toNanoOfDay() + 1;
        long randomNanoOfDay = RANDOM.nextLong(inclusiveMin, exclusiveMax);
        return LocalTime.ofNanoOfDay(randomNanoOfDay);
    }

    private LocalDate factoryDate() {
        long inclusiveMin = LocalDate.MIN.toEpochDay();
        long exclusiveMax = LocalDate.MAX.toEpochDay() + 1;
        long randomEpochDay = RANDOM.nextLong(inclusiveMin, exclusiveMax);
        return LocalDate.ofEpochDay(randomEpochDay);
    }
}
