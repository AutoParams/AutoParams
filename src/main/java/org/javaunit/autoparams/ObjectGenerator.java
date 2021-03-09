package org.javaunit.autoparams;

import java.util.Optional;
import java.util.Random;

interface ObjectGenerator {

    static final Random RANDOM = new Random();

    Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context);

}
