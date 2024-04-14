package autoparams.generator;

import java.util.stream.IntStream;

import autoparams.ResolutionContext;

final class IntStreamGenerator extends PlainObjectGenerator<IntStream> {

    IntStreamGenerator() {
        super(IntStream.class);
    }

    @Override
    protected IntStream generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return IntStream.generate(() -> context.resolve(int.class)).limit(3);
    }
}
