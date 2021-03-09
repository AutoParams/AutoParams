package org.javaunit.autoparams;

import java.util.Optional;
import java.util.Random;

interface ArgumentGenerator {

    static final Random RANDOM = new Random();

    Optional<Object> generate(ParameterDescriptor parameter, ArgumentGenerationContext context);

}
