package autoparams.generator;

import java.util.stream.DoubleStream;

import autoparams.ResolutionContext;

final class DoubleStreamGenerator extends ObjectGeneratorBase<DoubleStream> {

    @Override
    protected DoubleStream generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return DoubleStream
            .generate(() -> context.resolve(double.class))
            .limit(3);
    }
}
