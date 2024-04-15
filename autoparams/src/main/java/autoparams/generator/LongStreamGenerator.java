package autoparams.generator;

import java.util.stream.LongStream;

import autoparams.ResolutionContext;

final class LongStreamGenerator extends ObjectGeneratorBase<LongStream> {

    @Override
    protected LongStream generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return LongStream.generate(() -> context.resolve(long.class)).limit(3);
    }
}
