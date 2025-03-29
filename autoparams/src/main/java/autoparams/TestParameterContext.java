package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

import autoparams.AnnotationScanner.Edge;
import autoparams.customization.ArgumentProcessing;
import autoparams.customization.ArgumentProcessor;
import autoparams.customization.ArgumentRecycler;
import autoparams.customization.Customizer;
import autoparams.customization.RecycleArgument;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.commons.util.AnnotationUtils;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Instantiator.instantiate;
import static org.junit.jupiter.params.provider.Arguments.arguments;

final class TestParameterContext implements ParameterContext {

    private final TestResolutionContext resolutionContext;
    private final ParameterQuery query;

    public TestParameterContext(
        TestResolutionContext resolutionContext,
        ParameterQuery query
    ) {
        this.resolutionContext = resolutionContext;
        this.query = query;
    }

    @Override
    public Parameter getParameter() {
        return query.getParameter();
    }

    @Override
    public int getIndex() {
        return query.getIndex();
    }

    @Override
    public Optional<Object> getTarget() {
        return Optional.empty();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return AnnotationUtils.isAnnotated(
            query.getParameter(),
            query.getIndex(),
            annotationType
        );
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(
        Class<A> annotationType
    ) {
        return AnnotationUtils.findAnnotation(
            query.getParameter(),
            query.getIndex(),
            annotationType
        );
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(
        Class<A> annotationType
    ) {
        return AnnotationUtils.findRepeatableAnnotations(
            query.getParameter(),
            query.getIndex(),
            annotationType
        );
    }

    public Object resolveArgument() {
        Arguments emptyAsset = arguments();
        return resolveArgument(emptyAsset);
    }

    public Object resolveArgument(Arguments asset) {
        resolutionContext.applyAnnotatedCustomizers(query.getParameter());
        Object argument = supplyArgument(asset);
        recycleArgument(argument);
        return argument;
    }

    private Object supplyArgument(Arguments asset) {
        return query.getIndex() < asset.get().length
            ? convertArgument(asset.get()[query.getIndex()])
            : resolutionContext.resolve(query);
    }

    private Object convertArgument(Object source) {
        if (source instanceof Named<?>) {
            return convertArgument((Named<?>) source);
        } else if (source instanceof String) {
            return convertArgument((String) source);
        } else {
            return source;
        }
    }

    private Object convertArgument(Named<?> source) {
        Object payload = convertArgument(source.getPayload());
        return Named.of(source.getName(), payload);
    }

    private Object convertArgument(String source) {
        return getConverter()
            .convert(source, query)
            .orElseThrow(() -> {
                String message = "Cannot convert \"" + source
                    + "\" to an argument for " + query.getParameter() + ".";
                return new IllegalArgumentException(message);
            });
    }

    private StringConverter getConverter() {
        return resolutionContext.resolve(StringConverter.class);
    }

    @SuppressWarnings("deprecation")
    private void recycleArgument(Object argument) {
        if (argument instanceof Named<?>) {
            recycleArgument((Named<?>) argument);
            return;
        }

        for (Edge<RecycleArgument> edge :
            scanAnnotations(query.getParameter(), RecycleArgument.class)
        ) {
            recycleArgument(argument, edge);
        }

        for (Edge<ArgumentProcessing> edge :
            scanAnnotations(query.getParameter(), ArgumentProcessing.class)
        ) {
            recycleArgumentWithArgumentProcessor(argument, edge);
        }
    }

    private void recycleArgument(Named<?> argument) {
        recycleArgument(argument.getPayload());
    }

    private void recycleArgument(
        Object argument,
        Edge<RecycleArgument> edge
    ) {
        ArgumentRecycler recycler = instantiate(edge.getCurrent().value());
        edge.useParent(parent -> consumeAnnotationIfMatch(recycler, parent));
        Parameter parameter = query.getParameter();
        Customizer customizer = recycler.recycle(argument, parameter);
        resolutionContext.applyCustomizer(customizer);
    }

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    private void recycleArgumentWithArgumentProcessor(
        Object argument,
        Edge<ArgumentProcessing> edge
    ) {
        ArgumentProcessor processor = instantiate(edge.getCurrent().value());
        edge.useParent(parent -> consumeAnnotationIfMatch(processor, parent));
        Customizer customizer = processor.process(
            query.getParameter(),
            argument
        );
        resolutionContext.applyCustomizer(customizer);
    }
}
