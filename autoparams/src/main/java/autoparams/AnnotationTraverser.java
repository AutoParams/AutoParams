package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

final class AnnotationTraverser<T extends Annotation> {

    private final Class<T> annotationType;
    private final BiConsumer<T, Annotation> consumer;
    private final Set<Annotation> monitor;

    private AnnotationTraverser(
        Class<T> annotationType,
        BiConsumer<T, Annotation> consumer
    ) {
        this.annotationType = annotationType;
        this.consumer = consumer;
        this.monitor = new HashSet<>();
    }

    public static <T extends Annotation> void traverseAnnotations(
        AnnotatedElement element,
        Class<T> annotationType,
        BiConsumer<T, Annotation> consumer
    ) {
        new AnnotationTraverser<>(annotationType, consumer).traverse(element);
    }

    public static void traverseAnnotations(
        AnnotatedElement element,
        BiConsumer<Annotation, Annotation> consumer
    ) {
        traverseAnnotations(element, Annotation.class, consumer);
    }

    private void traverse(AnnotatedElement element) {
        traverse(element, null);
    }

    private void traverse(AnnotatedElement element, Annotation parent) {
        for (Annotation annotation : element.getAnnotations()) {
            if (monitor.add(annotation)) {
                try {
                    Class<?> annotationType = annotation.annotationType();
                    if (this.annotationType.isAssignableFrom(annotationType)) {
                        T match = this.annotationType.cast(annotation);
                        consumer.accept(match, parent);
                    }
                    traverse(annotationType, annotation);
                } finally {
                    monitor.remove(annotation);
                }
            }
        }
    }
}
