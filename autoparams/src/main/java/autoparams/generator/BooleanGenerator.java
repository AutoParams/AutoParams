package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class BooleanGenerator extends PrimitiveTypeGenerator<Boolean> {

    BooleanGenerator() {
        super(boolean.class, Boolean.class);
    }

    @Override
    protected Boolean generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return ThreadLocalRandom.current().nextInt() % 2 == 0;
    }
}
