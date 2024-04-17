package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.AnnotationUtils;

public final class ArgumentResolutionContext implements ParameterContext {

    private final ResolutionContext resolutionContext;
    private final Parameter parameter;
    private final int index;
    private final Object target;

    public ArgumentResolutionContext(
        ResolutionContext resolutionContext,
        Parameter parameter,
        int index
    ) {
        this(resolutionContext, parameter, index, null);
    }

    public ArgumentResolutionContext(
        ResolutionContext resolutionContext,
        Parameter parameter,
        int index,
        Object target
    ) {
        this.resolutionContext = resolutionContext;
        this.parameter = parameter;
        this.index = index;
        this.target = target;
    }

    public ResolutionContext getResolutionContext() {
        return resolutionContext;
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
        return Optional.ofNullable(target);
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
