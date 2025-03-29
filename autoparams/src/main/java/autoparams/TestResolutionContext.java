package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import autoparams.AnnotationScanner.Edge;
import autoparams.customization.ArgumentProcessing;
import autoparams.customization.ArgumentProcessor;
import autoparams.customization.ArgumentRecycler;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.customization.RecycleArgument;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Brake.collectBrakes;
import static autoparams.Instantiator.instantiate;

class TestResolutionContext extends ResolutionContext {

    private static final Object[] EMPTY_ASSET = new Object[0];

    private TestResolutionContext() {
    }

    static TestResolutionContext create(ExtensionContext extensionContext) {
        TestResolutionContext resolutionContext = new TestResolutionContext();
        resolutionContext.initialize(extensionContext);
        return resolutionContext;
    }

    private void initialize(ExtensionContext extensionContext) {
        applyCustomizer(new ExtensionContextProvider(extensionContext));
        applyAnnotatedCustomizers(extensionContext.getRequiredTestMethod());
    }

    private void applyAnnotatedCustomizers(AnnotatedElement element) {
        for (Edge<Annotation> edge : scanAnnotations(element)) {
            if (edge.getCurrent() instanceof Customization) {
                useCustomization(edge);
            } else if (edge.getCurrent() instanceof CustomizerSource) {
                useCustomizerSource(edge);
            }
        }
    }

    private void useCustomization(Edge<Annotation> edge) {
        Customization customization = (Customization) edge.getCurrent();
        for (Class<? extends Customizer> type : customization.value()) {
            applyCustomizer(instantiate(type));
        }
    }

    private void useCustomizerSource(Edge<Annotation> edge) {
        CustomizerSource source = (CustomizerSource) edge.getCurrent();
        CustomizerFactory factory = instantiate(source.value());
        edge.useParent(parent -> consumeAnnotationIfMatch(factory, parent));
        applyCustomizer(factory.createCustomizer());
    }

    public Arguments getTestCase(Arguments asset) {
        return getTestCase(asset.get());
    }

    public Arguments getTestCase(Object[] asset) {
        Brake brake = getBrake();
        Parameter[] parameters = getTestParameters();
        List<Object> arguments = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (brake.shouldBrakeBefore(parameter)) {
                break;
            }

            arguments.add(resolveArgument(getQuery(parameter, i), asset));
        }

        return Arguments.of(arguments.toArray());
    }

    private ParameterQuery getQuery(Parameter parameter, int index) {
        Type type = parameter.getParameterizedType();
        return new ParameterQuery(parameter, index, type);
    }

    private Brake getBrake() {
        List<Brake> brakes = new ArrayList<>();
        ExtensionContext extensionContext = resolve(ExtensionContext.class);
        collectBrakes(extensionContext.getRequiredTestMethod(), brakes::add);
        brakes.add(TestGearBrake.INSTANCE);
        return Brake.compose(brakes.toArray(new Brake[0]));
    }

    private Parameter[] getTestParameters() {
        ExtensionContext extensionContext = resolve(ExtensionContext.class);
        return extensionContext.getRequiredTestMethod().getParameters();
    }

    public Object resolveArgument(Parameter parameter, int index) {
        return resolveArgument(getQuery(parameter, index), EMPTY_ASSET);
    }

    private Object resolveArgument(ParameterQuery query, Object[] asset) {
        applyAnnotatedCustomizers(query.getParameter());
        Object argument = query.getIndex() < asset.length
            ? convertArgument(asset[query.getIndex()], query)
            : resolve(query);
        recycleArgument(argument, query);
        return argument;
    }

    @SuppressWarnings("deprecation")
    private void recycleArgument(Object argument, ParameterQuery query) {
        if (argument instanceof Named<?>) {
            recycleArgument((Named<?>) argument, query);
            return;
        }

        for (Edge<RecycleArgument> edge :
            scanAnnotations(query.getParameter(), RecycleArgument.class)
        ) {
            recycleArgument(argument, edge, query);
        }

        for (Edge<ArgumentProcessing> edge :
            scanAnnotations(query.getParameter(), ArgumentProcessing.class)
        ) {
            processArgument(argument, edge, query);
        }
    }

    private void recycleArgument(Named<?> argument, ParameterQuery query) {
        recycleArgument(argument.getPayload(), query);
    }

    private void recycleArgument(
        Object argument,
        Edge<RecycleArgument> edge,
        ParameterQuery query
    ) {
        ArgumentRecycler recycler = instantiate(edge.getCurrent().value());
        edge.useParent(parent -> consumeAnnotationIfMatch(recycler, parent));
        applyCustomizer(recycler.recycle(argument, query.getParameter()));
    }

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    private void processArgument(
        Object argument,
        Edge<ArgumentProcessing> edge,
        ParameterQuery query
    ) {
        ArgumentProcessor processor = instantiate(edge.getCurrent().value());
        edge.useParent(parent -> consumeAnnotationIfMatch(processor, parent));
        applyCustomizer(processor.process(query.getParameter(), argument));
    }

    private Object convertArgument(Object source, ParameterQuery query) {
        if (source instanceof Named<?>) {
            return convertArgument((Named<?>) source, query);
        } else if (source instanceof String) {
            return convertArgument((String) source, query);
        } else {
            return source;
        }
    }

    private Object convertArgument(Named<?> source, ParameterQuery query) {
        Object payload = convertArgument(source.getPayload(), query);
        return Named.of(source.getName(), payload);
    }

    private Object convertArgument(String source, ParameterQuery query) {
        StringConverter converter = resolve(StringConverter.class);
        return converter.convert(source, query).orElseThrow(() -> {
            String message = "Cannot convert \"" + source
                + "\" to an argument for " + query.getParameter() + ".";
            return new IllegalArgumentException(message);
        });
    }
}
