package autoparams.generator;

import java.util.stream.IntStream;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSize;

final class IntStreamGenerator extends ObjectGeneratorBase<IntStream> {

    @Override
    protected IntStream generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        int size = getSize(query);
        return IntStream.generate(() -> context.resolve(int.class)).limit(size);
    }
}
