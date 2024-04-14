package autoparams.generator;

import java.util.stream.DoubleStream;

import autoparams.ResolutionContext;

final class DoubleStreamGenerator extends PlainObjectGenerator<DoubleStream> {

    DoubleStreamGenerator() {
        super(DoubleStream.class);
    }

    @Override
    protected DoubleStream generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return DoubleStream
            .generate(() -> context.resolve(double.class))
            .limit(3);
    }
}
