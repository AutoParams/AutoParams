package org.javaunit.autoparams;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

interface ObjectGenerator {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context);

}
