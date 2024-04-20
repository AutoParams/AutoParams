package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

final class AnnotationParameterScanBrake implements
    ParameterScanBrake,
    AnnotationConsumer<BrakeBeforeAnnotation> {

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation>[] annotationTypes = new Class[0];

    @Override
    public boolean shouldBrakeBefore(ParameterContext parameterContext) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            Parameter parameter = parameterContext.getParameter();
            int index = parameterContext.getIndex();
            if (isAnnotated(parameter, index, annotationType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void accept(BrakeBeforeAnnotation brakeBeforeAnnotation) {
        annotationTypes = brakeBeforeAnnotation.value();
    }
}
