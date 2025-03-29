package autoparams;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.function.Consumer;

import autoparams.AnnotationScanner.Edge;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Instantiator.instantiate;

@FunctionalInterface
interface Brake {

    boolean shouldBrakeBefore(Parameter parameter);

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
        return parameter -> {
            for (Brake brake : brakes) {
                if (brake.shouldBrakeBefore(parameter)) {
                    return true;
                }
            }

            return false;
        };
    }
}
