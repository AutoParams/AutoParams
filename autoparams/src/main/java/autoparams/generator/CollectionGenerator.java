package autoparams.generator;

import java.util.function.IntSupplier;
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
            new SetGenerator()
        );
    }

    static IntSupplier getSizeSupplier(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? getSizeSupplier((ParameterQuery) query)
            : CollectionGenerator::getDefaultSize;
    }

    private static IntSupplier getSizeSupplier(ParameterQuery query) {
        Size size = query.getParameter().getAnnotation(Size.class);
        return size == null ? CollectionGenerator::getDefaultSize : size::min;
    }

    private static int getDefaultSize() {
        return DEFAULT_SIZE;
    }
}
