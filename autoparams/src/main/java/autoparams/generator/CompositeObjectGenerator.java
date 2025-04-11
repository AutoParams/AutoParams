package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.internal.Folder.foldl;
import static java.util.Arrays.stream;

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
        return foldl(
            (result, generator) -> result
                .yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            stream(generators)
        );
    }
}
