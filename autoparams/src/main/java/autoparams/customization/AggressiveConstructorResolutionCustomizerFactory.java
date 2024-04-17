package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

@SuppressWarnings("deprecation")
public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationConsumer<ResolveConstructorAggressively>,
    AnnotationVisitor<ResolveConstructorAggressively>,
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
