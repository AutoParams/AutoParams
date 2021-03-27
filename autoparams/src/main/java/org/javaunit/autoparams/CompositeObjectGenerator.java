package org.javaunit.autoparams;

import java.util.Optional;

class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        for (ObjectGenerator generator : generators) {
            Optional<Object> argument = generator.generate(query, context);
            if (argument.isPresent()) {
                return argument;
            }
        }

        return Optional.empty();
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
