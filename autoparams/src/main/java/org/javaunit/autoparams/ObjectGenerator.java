package org.javaunit.autoparams;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

interface ObjectGenerator {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context);

    default GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        return this.generate(query, context)
            .map(GenerationResult::success)
            .orElseGet(GenerationResult::failure);
    }

}
