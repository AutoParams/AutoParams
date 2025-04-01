package autoparams.customization;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

class ImplementationGenerator implements ObjectGenerator {

    private final Class<?> type;

    public ImplementationGenerator(Class<?> type) {
        this.type = type;
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return new TypeLens(type).implementsInterface(query.getType())
            ? new ObjectContainer(context.resolve(type))
            : ObjectContainer.EMPTY;
    }
}
