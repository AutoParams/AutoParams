package autoparams.generator;

import java.util.stream.LongStream;

import autoparams.ResolutionContext;

final class LongStreamGenerator extends PlainObjectGenerator<LongStream> {

    LongStreamGenerator() {
        super(LongStream.class);
    }

    @Override
    protected LongStream generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return LongStream.generate(() -> context.resolve(long.class)).limit(3);
    }
}
