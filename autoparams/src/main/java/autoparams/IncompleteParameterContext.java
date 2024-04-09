package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.AnnotationUtils;

final class IncompleteParameterContext implements ParameterContext {

    private final Parameter parameter;
    private final int index;

    public IncompleteParameterContext(Parameter parameter) {
        this.parameter = parameter;
        this.index = inferIndex(parameter);
    }

    private static int inferIndex(Parameter parameter) {
        Parameter[] parameters = parameter
            .getDeclaringExecutable()
            .getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].equals(parameter)) {
                return i;
            }
        }

        throw new IllegalArgumentException("Cannot infer index of parameter.");
    }

    @Override
    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Optional<Object> getTarget() {
        return Optional.empty();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return AnnotationUtils.isAnnotated(
            this.parameter,
            this.index,
            annotationType
        );
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(
        Class<A> annotationType
    ) {
        return AnnotationUtils.findAnnotation(
            this.parameter,
            this.index,
            annotationType
        );
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(
        Class<A> annotationType
    ) {
        return AnnotationUtils.findRepeatableAnnotations(
            this.parameter,
            this.index,
            annotationType
        );
    }
}
