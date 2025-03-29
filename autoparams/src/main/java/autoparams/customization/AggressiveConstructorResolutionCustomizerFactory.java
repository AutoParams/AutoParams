package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationConsumer<ResolveConstructorAggressively>,
    CustomizerFactory {

    private Class<?> target;

    @Override
    public Customizer createCustomizer() {
        return new AggressiveConstructorResolutionCustomizer(target);
    }

    @Override
    public void accept(ResolveConstructorAggressively annotation) {
        this.target = annotation.value();
    }
}
