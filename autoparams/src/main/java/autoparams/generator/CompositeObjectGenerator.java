package autoparams.generator;

import autoparams.ResolutionContext;
import java.util.Arrays;

public class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return Folder.foldl(
            (result, generator) -> result.yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            Arrays.stream(generators)
        );
    }
}
