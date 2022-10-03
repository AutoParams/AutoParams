package autoparams.customization;

import java.lang.annotation.Annotation;

public interface AnnotationVisitor<T extends Annotation> {

    void visit(T annotation);

}
