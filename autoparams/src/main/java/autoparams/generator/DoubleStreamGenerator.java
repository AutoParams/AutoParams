package autoparams.generator;

import java.util.stream.DoubleStream;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSize;

final class DoubleStreamGenerator extends ObjectGeneratorBase<DoubleStream> {

    @Override
    protected DoubleStream generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return DoubleStream
            .generate(() -> context.resolve(double.class))
            .limit(getSize(query));
    }
}
