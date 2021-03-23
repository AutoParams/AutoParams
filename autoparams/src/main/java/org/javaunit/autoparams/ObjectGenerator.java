package org.javaunit.autoparams;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

interface ObjectGenerator {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * This method generates an arbitrary object with the given query.
     *
     * @deprecated This method will be deleted, so don't implement it, but implement
     *     the {@link ObjectGenerator#generateObject(ObjectQuery, ObjectGenerationContext)} method
     *     instead.
     */
    Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context);

    default GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        return this.generate(query, context)
            .map(GenerationResult::success)
            .orElseGet(GenerationResult::failure);
    }

}
