package autoparams.customization;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;

class GreediestConstructorCustomizerFactory implements
    AnnotatedElementConsumer,
    CustomizerFactory {

    private Class<?> type;

    @Override
    public Customizer createCustomizer() {
        return new AggressiveConstructorResolutionCustomizer(type);
    }

    @Override
    public void accept(AnnotatedElement element) {
        if (element instanceof Parameter) {
            type = ((Parameter) element).getType();
        }
    }
}
