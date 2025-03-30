package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

class ImplementationGeneratorFactory implements
    AnnotationConsumer<UseImplementation>,
    CustomizerFactory {

    private ImplementationGenerator generator;

    @Override
    public Customizer createCustomizer() {
        return generator;
    }

    @Override
    public void accept(UseImplementation annotation) {
        generator = new ImplementationGenerator(annotation.value());
    }
}
