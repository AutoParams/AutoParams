package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.stream;

class ImplementationGeneratorCompositeFactory implements
    AnnotationConsumer<UseImplementation>,
    CustomizerFactory {

    private Class<?>[] types;

    @Override
    public Customizer createCustomizer() {
        return new CompositeCustomizer(
            stream(types)
                .map(ImplementationGenerator::new)
                .toArray(Customizer[]::new)
        );
    }

    @Override
    public void accept(UseImplementation annotation) {
        this.types = annotation.value();
    }
}
