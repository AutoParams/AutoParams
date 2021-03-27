package org.javaunit.autoparams;

import java.util.Optional;

class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        throw new UnsupportedOperationException(MESSAGE_FOR_UNSUPPORTED_GENERATE_METHOD);
    }

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        for (ObjectGenerator generator : generators) {
            GenerationResult result = generator.generateObject(query, context);
            if (result.isPresent()) {
                return result;
            }
        }

        return GenerationResult.absence();
    }
}
