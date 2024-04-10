package autoparams.generator;

import autoparams.ResolutionContext;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;

final class ArgumentConverterGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType().equals(ArgumentConverter.class)) {
            return new ObjectContainer(DefaultArgumentConverter.INSTANCE);
        } else {
            return ObjectContainer.EMPTY;
        }
    }
}
