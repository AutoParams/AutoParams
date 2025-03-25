package autoparams.spring;

import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;

class BeanGeneratorFactory implements CustomizerFactory {

    @Override
    public Customizer createCustomizer() {
        return new BeanGenerator();
    }
}
