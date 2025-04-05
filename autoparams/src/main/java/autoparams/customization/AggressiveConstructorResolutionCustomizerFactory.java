package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationConsumer<ResolveConstructorAggressively>,
    CustomizerFactory {

    private Class<?>[] types;

    @Override
    public Customizer createCustomizer() {
        return new AggressiveConstructorResolutionCustomizer(types);
    }

    @Override
    public void accept(ResolveConstructorAggressively annotation) {
        this.types = annotation.value();
    }
}
