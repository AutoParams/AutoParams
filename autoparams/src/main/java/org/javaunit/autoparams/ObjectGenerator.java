package org.javaunit.autoparams;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

interface ObjectGenerator {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    String MESSAGE_FOR_UNSUPPORTED_GENERATE_METHOD =
        "This generate method is not supported. Use the generateObject method instead.";

    Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context);

    default GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        return this.generate(query, context)
            .map(GenerationResult::presence)
            .orElseGet(GenerationResult::absence);
    }

}
