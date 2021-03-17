package org.javaunit.autoparams;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JavaTimeObject {

    private final LocalDate localDate;
    private final LocalTime localTime;
    private final LocalDateTime localDateTime;

    public JavaTimeObject(LocalDate localDate, LocalTime localTime, LocalDateTime localDateTime) {
        this.localDate = localDate;
        this.localTime = localTime;
        this.localDateTime = localDateTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

}
