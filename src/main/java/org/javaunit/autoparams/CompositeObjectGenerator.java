package org.javaunit.autoparams;

import java.util.Optional;

final class CompositeObjectGenerator implements ObjectGenerator {

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

}
