package org.javaunit.autoparams.generator;

import java.util.Arrays;

public class CompositeObjectGenerator implements ObjectGenerator {

    private ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return Folder.foldl(
            (result, generator) -> result.yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            Arrays.stream(generators)
        );
    }

}
