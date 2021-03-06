package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Optional;

final class BigDecimalArgumentGenerator implements ArgumentGenerator {

    @Override
    public Optional<Object> generate(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.equals(BigDecimal.class) ? factory() : empty;
    }

    private Optional<Object> factory() {
        return Optional.of(new BigDecimal(random.nextInt()));
    }

}
