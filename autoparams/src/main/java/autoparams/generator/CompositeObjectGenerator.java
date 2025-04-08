package autoparams.generator;

import java.util.Arrays;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.internal.Folder;

public class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return Folder.foldl(
            (result, generator) -> result
                .yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            Arrays.stream(generators)
        );
    }
}
