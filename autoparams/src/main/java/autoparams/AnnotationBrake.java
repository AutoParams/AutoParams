package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import org.junit.jupiter.params.support.AnnotationConsumer;

final class AnnotationBrake implements
    Brake,
    AnnotationConsumer<BrakeBeforeAnnotation> {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] EMPTY = new Class[0];

    private Class<? extends Annotation>[] annotationTypes = EMPTY;

    @Override
    public boolean shouldBrakeBefore(Parameter parameter) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (parameter.getAnnotation(annotationType) != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void accept(BrakeBeforeAnnotation annotation) {
        annotationTypes = annotation.value();
    }
}
