package autoparams.customization;

import java.lang.reflect.Parameter;
import java.util.function.BiPredicate;

import autoparams.ObjectQuery;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.internal.Folder.foldl;
import static java.lang.System.arraycopy;
import static java.util.Arrays.stream;

class FreezingRecycler implements
    ArgumentRecycler,
    AnnotationConsumer<FreezeBy> {

    private static class Negative<T, U> implements BiPredicate<T, U> {

        @Override
        public boolean test(T t, U u) {
            return false;
        }
    }

    private static final BiPredicate<Parameter, ObjectQuery> NEGATIVE;
    private static final Matching[] EMPTY;

    static {
        NEGATIVE = new Negative<>();
        EMPTY = new Matching[0];
    }

    private Matching[] predicates = EMPTY;

    void setPredicates(Matching[] predicates) {
        Matching[] array = new Matching[predicates.length];
        arraycopy(predicates, 0, array, 0, predicates.length);
        this.predicates = array;
    }

    @Override
    public Customizer recycle(Object argument, Parameter parameter) {
        return getGenerator(argument, parameter);
    }

    private ObjectGenerator getGenerator(Object argument, Parameter parameter) {
        BiPredicate<Parameter, ObjectQuery> predicate = foldl(
            BiPredicate::or,
            NEGATIVE,
            stream(predicates)
        );

        return (query, context) -> predicate.test(parameter, query)
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }

    @Override
    public void accept(FreezeBy freezeBy) {
        setPredicates(freezeBy.value());
    }
}
