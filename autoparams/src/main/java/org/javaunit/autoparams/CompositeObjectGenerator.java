package org.javaunit.autoparams;

class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
        for (ObjectGenerator generator : generators) {
            GenerationResult result = generator.generate(query, context);
            if (result.isPresent()) {
                return result;
            }
        }

        return GenerationResult.absence();
    }

}
