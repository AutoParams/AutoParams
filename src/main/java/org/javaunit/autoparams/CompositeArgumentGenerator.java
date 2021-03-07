package org.javaunit.autoparams;

import java.util.Optional;

final class CompositeArgumentGenerator implements ArgumentGenerator {

    private final ArgumentGenerator[] generators;

    public CompositeArgumentGenerator(ArgumentGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public Optional<Object> generate(ArgumentGenerationContext context) {
        for (ArgumentGenerator generator : generators) {
            Optional<Object> argument = generator.generate(context);
            if (argument.isPresent()) {
                return argument;
            }
        }

        return Optional.empty();
    }

}
