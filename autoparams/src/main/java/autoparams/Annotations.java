package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.Instantiator.instantiate;

final class Annotations {

    private static void traverseAnnotations(
        AnnotatedElement annotated,
        Predicate<Annotation> predicate,
        Consumer<Annotation> consumer,
        Set<Annotation> monitor
    ) {
        for (Annotation annotation : annotated.getAnnotations()) {
            if (monitor.add(annotation)) {
                try {
                    if (predicate.test(annotation)) {
                        consumer.accept(annotation);
                    }
                    traverseAnnotations(
                        annotation.annotationType(),
                        predicate,
                        consumer,
                        monitor
                    );
                } finally {
                    monitor.remove(annotation);
                }
            }
        }
    }

    public static void traverseAnnotations(
        AnnotatedElement annotated,
        Predicate<Annotation> predicate,
        Consumer<Annotation> consumer
    ) {
        Set<Annotation> monitor = new HashSet<>();
        traverseAnnotations(annotated, predicate, consumer, monitor);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> void traverseAnnotations(
        AnnotatedElement annotated,
        Class<T> annotationType,
        Consumer<T> consumer
    ) {
        traverseAnnotations(
            annotated,
            annotation -> annotation.annotationType().equals(annotationType),
            annotation -> consumer.accept((T) annotation)
        );
    }

    public static void traverseAnnotations(
        AnnotatedElement annotated,
        Consumer<Annotation> consumer
    ) {
        traverseAnnotations(annotated, annotation -> true, consumer);
    }

    public static <L extends Annotation, S> Optional<S> findService(
        Annotation annotation,
        Class<? extends L> locatorType,
        Function<L, Class<? extends S>> locateServiceType
    ) {
        return Optional
            .ofNullable(annotation.annotationType().getAnnotation(locatorType))
            .map(locator -> getService(annotation, locator, locateServiceType));
    }

    @SuppressWarnings("unchecked")
    private static <L extends Annotation, S> S getService(
        Annotation annotation,
        L locator,
        Function<L, Class<? extends S>> locateServiceType
    ) {
        S service = instantiate(locateServiceType.apply(locator));
        if (service instanceof AnnotationConsumer<?>) {
            ((AnnotationConsumer<Annotation>) service).accept(annotation);
        }
        return service;
    }
}
