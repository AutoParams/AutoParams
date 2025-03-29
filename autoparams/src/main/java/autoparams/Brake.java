package autoparams;

import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;

import autoparams.AnnotationScanner.Edge;
import org.junit.jupiter.api.extension.ParameterContext;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Instantiator.instantiate;

@FunctionalInterface
interface Brake {

    boolean shouldBrakeBefore(ParameterContext parameterContext);

    static void collectBrakes(
        AnnotatedElement element,
        Consumer<Brake> collector
    ) {
        for (Edge<BrakeWith> edge : scanAnnotations(element, BrakeWith.class)) {
            Brake brake = instantiate(edge.getCurrent().value());
            edge.useParent(parent -> consumeAnnotationIfMatch(brake, parent));
            collector.accept(brake);
        }
    }

    static Brake compose(Brake... brakes) {
        return parameterContext -> {
            for (Brake brake : brakes) {
                if (brake.shouldBrakeBefore(parameterContext)) {
                    return true;
                }
            }

            return false;
        };
    }
}
