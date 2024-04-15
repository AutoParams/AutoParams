package autoparams.generator;

import java.util.stream.IntStream;

import autoparams.ResolutionContext;

final class IntStreamGenerator extends ObjectGeneratorBase<IntStream> {

    @Override
    protected IntStream generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return IntStream.generate(() -> context.resolve(int.class)).limit(3);
    }
}
