package autoparams;

import java.util.function.Predicate;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class PredicateBrake implements
    Brake,
    AnnotationConsumer<BrakeBefore> {

    private static final Predicate<ParameterContext> NEGATIVE = x -> false;

    private Predicate<ParameterContext> predicate = NEGATIVE;

    @Override
    public boolean shouldBrakeBefore(ParameterContext parameterContext) {
        return predicate.test(parameterContext);
    }

    @Override
    public void accept(BrakeBefore annotation) {
        predicate = Instantiator.instantiate(annotation.value());
    }
}
