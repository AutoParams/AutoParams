package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import autoparams.AnnotationScanner.Edge;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Brake.collectBrakes;
import static autoparams.Instantiator.instantiate;
import static org.junit.jupiter.params.provider.Arguments.arguments;

final class TestCaseContext extends ResolutionContext {

    private final Method method;
    private final Brake brake;

    public TestCaseContext(ExtensionContext extensionContext) {
        this(extensionContext, extensionContext.getRequiredTestMethod());
    }

    private TestCaseContext(ExtensionContext extensionContext, Method method) {
        this(extensionContext, method, getBrake(method));
    }

    private TestCaseContext(
        ExtensionContext extensionContext,
        Method method,
        Brake brake
    ) {
        super(extensionContext);
        this.method = method;
        this.brake = brake;
    }

    private static Brake getBrake(Method method) {
        List<Brake> brakes = new ArrayList<>();
        collectBrakes(method, brakes::add);
        brakes.add(TestGearBrake.INSTANCE);
        return Brake.compose(brakes.toArray(new Brake[0]));
    }

    public Arguments getTestCase(Arguments asset) {
        applyCustomizers(method);
        List<Object> arguments = new ArrayList<>();
        for (TestParameterContext parameterContext : getParameterContexts()) {
            if (brake.shouldBrakeBefore(parameterContext)) {
                break;
            }
            applyCustomizers(parameterContext.getParameter());
            arguments.add(parameterContext.resolveArgument(asset));
        }
        return arguments(arguments.toArray());
    }

    private void applyCustomizers(AnnotatedElement element) {
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

    private TestParameterContext[] getParameterContexts() {
        Parameter[] parameters = method.getParameters();
        List<TestParameterContext> contexts = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            contexts.add(new TestParameterContext(this, parameters[i], i));
        }
        return contexts.toArray(new TestParameterContext[parameters.length]);
    }
}
