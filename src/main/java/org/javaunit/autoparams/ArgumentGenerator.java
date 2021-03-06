package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Random;

interface ArgumentGenerator {

    static final Random random = new Random();
    static final Optional<Object> empty = Optional.empty();

    Optional<Object> generate(Parameter parameter);

}
