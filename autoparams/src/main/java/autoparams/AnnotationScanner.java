package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

final class AnnotationScanner {

    public static <T extends Annotation> List<Edge<T>> scanAnnotations(
        AnnotatedElement element,
        Class<T> annotationType
    ) {
        ScanContext<T> context = new ScanContext<>(annotationType);
        scanAnnotations(element, null, context);
        return Collections.unmodifiableList(context.edges);
    }

    public static List<Edge<Annotation>> scanAnnotations(
        AnnotatedElement element
    ) {
        return scanAnnotations(element, Annotation.class);
    }

    private static <T extends Annotation> void scanAnnotations(
        AnnotatedElement element,
        Annotation parent,
        ScanContext<T> context
    ) {
        for (Annotation current : element.getAnnotations()) {
            if (context.monitor.add(current)) {
                try {
                    context.addIfMatch(current, parent);
                    scanAnnotations(current.annotationType(), current, context);
                } finally {
                    context.monitor.remove(current);
                }
            }
        }
    }

    private static final class ScanContext<T extends Annotation> {

        private final Class<T> annotationType;
        private final List<Edge<T>> edges;
        private final Set<Annotation> monitor;

        public ScanContext(Class<T> annotationType) {
            this.annotationType = annotationType;
            this.edges = new ArrayList<>();
            this.monitor = new HashSet<>();
        }

        public void addIfMatch(Annotation current, Annotation parent) {
            Class<?> annotationType = current.annotationType();
            if (this.annotationType.isAssignableFrom(annotationType)) {
                T match = this.annotationType.cast(current);
                edges.add(new Edge<>(match, parent));
            }
        }
    }

    public static final class Edge<T extends Annotation> {

        private final T current;
        private final Annotation parent;

        public Edge(T current, Annotation parent) {
            this.current = current;
            this.parent = parent;
        }

        public T getCurrent() {
            return current;
        }

        public void useParent(Consumer<Annotation> consumer) {
            if (parent != null) {
                consumer.accept(parent);
            }
        }
    }
}
