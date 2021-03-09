package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Random;

interface ArgumentGenerator {

    static final Random RANDOM = new Random();

    Optional<Object> generate(Parameter parameter, ArgumentGenerationContext context);

}
