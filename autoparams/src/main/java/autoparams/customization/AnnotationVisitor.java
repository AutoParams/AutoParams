package autoparams.customization;

import java.lang.annotation.Annotation;

import org.junit.jupiter.params.support.AnnotationConsumer;

@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public interface AnnotationVisitor<T extends Annotation>
    extends AnnotationConsumer<T> {

    @SuppressWarnings("unused")
    default void visit(T annotation) {
        accept(annotation);
    }
}
