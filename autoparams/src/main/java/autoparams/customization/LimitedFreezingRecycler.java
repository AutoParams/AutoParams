package autoparams.customization;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.support.AnnotationConsumer;

final class LimitedFreezingRecycler implements
    ArgumentRecycler,
    AnnotationConsumer<Freeze> {

    private final FreezingRecycler freezingRecycler = new FreezingRecycler();

    @Override
    public Customizer recycle(Object argument, Parameter parameter) {
        return freezingRecycler.recycle(argument, parameter);
    }

    @Override
    public void accept(Freeze annotation) {
        List<Matching> predicates = new ArrayList<>();

        if (annotation.byExactType()) {
            predicates.add(Matching.EXACT_TYPE);
        }

        if (annotation.byImplementedInterfaces()) {
            predicates.add(Matching.IMPLEMENTED_INTERFACES);
        }

        freezingRecycler.setPredicates(predicates.toArray(new Matching[0]));
    }
}
