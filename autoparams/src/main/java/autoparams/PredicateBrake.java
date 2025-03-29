package autoparams;

import java.lang.reflect.Parameter;
import java.util.function.Predicate;

import org.junit.jupiter.params.support.AnnotationConsumer;

final class PredicateBrake implements
    Brake,
    AnnotationConsumer<BrakeBefore> {

    private static final Predicate<Parameter> NEGATIVE = x -> false;

    private Predicate<Parameter> predicate = NEGATIVE;

    @Override
    public boolean shouldBrakeBefore(Parameter parameter) {
        return predicate.test(parameter);
    }

    @Override
    public void accept(BrakeBefore annotation) {
        predicate = Instantiator.instantiate(annotation.value());
    }
}
