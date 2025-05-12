package autoparams.generator;

import javax.validation.constraints.Size;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;

final class CollectionGenerator extends CompositeObjectGenerator {

    private static final int DEFAULT_SIZE = 3;

    public CollectionGenerator() {
        super(
            new ArrayGenerator(),
            new SequenceGenerator(),
            new MapGenerator(),
            new SetGenerator(),
            new StreamGenerator()
        );
    }

    static int getSize(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getSize((ParameterQuery) query)
            : DEFAULT_SIZE;
    }

    private static int getSize(ParameterQuery query) {
        Size size = query.getParameter().getAnnotation(Size.class);
        return size == null ? DEFAULT_SIZE : size.min();
    }
}
