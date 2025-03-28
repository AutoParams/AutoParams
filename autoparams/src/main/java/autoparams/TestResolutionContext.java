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

import static autoparams.AnnotationConsumption.consumeAnnotationIfMatch;
import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Instantiator.instantiate;

class TestResolutionContext extends ResolutionContext {

    private TestResolutionContext() {
    }

    static TestResolutionContext create(ExtensionContext extensionContext) {
        TestResolutionContext resolutionContext = new TestResolutionContext();
        resolutionContext.initialize(extensionContext);
        return resolutionContext;
    }

    public void initialize(ExtensionContext extensionContext) {
        applyCustomizer(new ExtensionContextProvider(extensionContext));
        applyAnnotatedCustomizers(extensionContext.getRequiredTestMethod());
    }

    public void applyAnnotatedCustomizers(AnnotatedElement element) {
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

    TestParameterContext[] getParameterContexts(
        ExtensionContext extensionContext
    ) {
        Method testMethod = extensionContext.getRequiredTestMethod();
        Parameter[] parameters = testMethod.getParameters();
        List<TestParameterContext> contexts = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            contexts.add(new TestParameterContext(this, parameters[i], i));
        }

        return contexts.toArray(new TestParameterContext[parameters.length]);
    }
}
