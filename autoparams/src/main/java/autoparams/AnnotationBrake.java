package autoparams;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class AnnotationBrake implements
    Brake,
    AnnotationConsumer<BrakeBeforeAnnotation> {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] EMPTY = new Class[0];

    private Class<? extends Annotation>[] annotationTypes = EMPTY;

    @Override
    public boolean shouldBrakeBefore(ParameterContext parameterContext) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (parameterContext.isAnnotated(annotationType)) {
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
